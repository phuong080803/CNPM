from flask import Flask, jsonify, Response
import cv2
import numpy as np
import pickle
from sklearn.svm import SVC
from datetime import datetime
import os
import csv
import mysql.connector
import time
from win32com.client import Dispatch
from flask_cors import CORS
from flask import request

# Định nghĩa key của bạn
API_KEY = '720fb192c4e62942b3d6a0bee0d1bda2ed17462a76c1308bf0cca30755f73381'  # Thay thế bằng key của bạn

def verify_api_key():
    api_key = request.headers.get('Authorization')
    if api_key != f'Bearer {API_KEY}':
        return False
    return True

app = Flask(__name__)
CORS(app)  # Kích hoạt CORS
data_folder = 'D:/data/'
face_cascade = cv2.CascadeClassifier('D:/face/haarcascade_frontalface_default.xml')
# Tải mô hình SVM và bộ mã hóa nhãn
with open('D:/data/svm_model.pkl', 'rb') as f:
    svm_model = pickle.load(f)
with open('D:/data/label_encoder.pkl', 'rb') as f:
    label_encoder = pickle.load(f)

# Đường dẫn file Haarcascade để phát hiện khuôn mặt
face_cascade = cv2.CascadeClassifier('D:/face/haarcascade_frontalface_default.xml')

# Kết nối với MySQL
connection = mysql.connector.connect(
    host="localhost",
    user="root",  # Thay bằng tên người dùng MySQL
    password="123456",  # Thay bằng mật khẩu MySQL
    database="user",
    port=3406
)
cursor = connection.cursor()
def connect_db():
    return mysql.connector.connect(
        host="localhost",
        user="root",  # Thay bằng tên người dùng MySQL của bạn
        password="123456",  # Thay bằng mật khẩu MySQL của bạn
        database="user",  # Thay bằng tên cơ sở dữ liệu của bạn
        port=3406  # Thay bằng cổng MySQL của bạn nếu khác
    )
# Ngưỡng xác suất để xác định "unknown"
prob_threshold = 0.75  # Bạn có thể điều chỉnh giá trị này

# Khởi động webcam
video = cv2.VideoCapture(0)

# Hàm nói
def speak(text):
    speaker = Dispatch("SAPI.SpVoice")
    speaker.Speak(text)

# Hàm nhận diện gương mặt
def recognize_face(frame):
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
    faces = face_cascade.detectMultiScale(gray, 1.3, 5)

    predicted_name = "Unknown"
    predicted_id = None
    for (x, y, w, h) in faces:
        crop_img = frame[y:y+h, x:x+w]
        resized_img = cv2.resize(crop_img, (128, 128)).flatten().reshape(1, -1)

        # Dự đoán với mô hình SVM
        probabilities = svm_model.predict_proba(resized_img)
        max_prob = np.max(probabilities)
        predicted_class = np.argmax(probabilities)

        if max_prob > prob_threshold:
            predicted_id = label_encoder.inverse_transform([predicted_class])[0]
            # Truy xuất tên từ database MySQL
            sql = "SELECT name FROM employee WHERE id_employee = %s"
            cursor.execute(sql, (str(predicted_id),))
            results = cursor.fetchall()
            if results:
                name = results[0][0]
                predicted_name = name.upper() + "-" + str(predicted_id)
        else:
            predicted_name = "Unknown"
    
    return predicted_name, predicted_id

# Hàm ghi dữ liệu chấm công vào database
def log_attendance(predicted_id, status):
    ts = time.time()
    date = datetime.fromtimestamp(ts).strftime("%y-%m-%d")
    timestamp = datetime.fromtimestamp(ts).strftime("%H:%M:%S")

    # Check-in
    if status == "checkin":
        check_checkin = "SELECT COUNT(*) FROM attendence WHERE id_employee=%s and date=%s "
        cursor.execute(check_checkin, (str(predicted_id), str(date)))
        count = cursor.fetchall()[0][0]

        if count == 0:  # Nếu chưa có lần checkin trong ngày
            sql = "INSERT INTO attendence(id_employee, date, time_in) VALUES (%s, %s, %s)"
            cursor.execute(sql, (str(predicted_id), str(date), str(timestamp)))
            connection.commit()
            speak(f"{predicted_id} đã chấm công vào")

    # Check-out
    elif status == "checkout":
        check_checkout = "SELECT COUNT(*) FROM attendence WHERE id_employee=%s and date=%s"
        cursor.execute(check_checkout, (str(predicted_id), str(date)))
        count = cursor.fetchall()[0][0]

        if count == 1:  # Nếu đã checkin trước đó
            sql = "UPDATE attendence SET time_out=%s WHERE id_employee=%s and date=%s"
            cursor.execute(sql, (str(timestamp), str(predicted_id), str(date)))
            connection.commit()
            speak(f"{predicted_id} đã chấm công ra")

# API xử lý Clock In
@app.route('/clockin', methods=['POST'])
def clockin():
    ret, frame = video.read()
    if not ret:
        return jsonify({"error": "Không thể đọc khung hình từ camera"}), 500

    # Nhận diện khuôn mặt
    predicted_name, predicted_id = recognize_face(frame)
    if predicted_name != "Unknown":
        log_attendance(predicted_id, "checkin")

    return jsonify({"name": predicted_name, "status": "clockin", "timestamp": datetime.now().strftime("%Y-%m-%d %H:%M:%S")})

# API xử lý Clock Out
@app.route('/clockout', methods=['POST'])
def clockout():
    ret, frame = video.read()
    if not ret:
        return jsonify({"error": "Không thể đọc khung hình từ camera"}), 500

    # Nhận diện khuôn mặt
    predicted_name, predicted_id = recognize_face(frame)
    if predicted_name != "Unknown":
        log_attendance(predicted_id, "checkout")

    return jsonify({"name": predicted_name, "status": "clockout", "timestamp": datetime.now().strftime("%Y-%m-%d %H:%M:%S")})

# API truyền phát video trực tiếp
@app.route('/video_feed')
def video_feed():
    def generate_frames():
        while True:
            success, frame = video.read()
            if not success:
                break
            else:
                predicted_name, _ = recognize_face(frame)
                cv2.putText(frame, predicted_name, (50, 50), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 0), 2, cv2.LINE_AA)
                ret, buffer = cv2.imencode('.jpg', frame)
                frame = buffer.tobytes()
                yield (b'--frame\r\n'
                       b'Content-Type: image/jpeg\r\n\r\n' + frame + b'\r\n')

    return Response(generate_frames(), mimetype='multipart/x-mixed-replace; boundary=frame')
# API lấy danh sách chấm công
@app.route('/attendances', methods=['GET'])
def get_attendances():
    if not verify_api_key():
        return jsonify({"error":"Invalid API key."}), 401  # Nếu không có key hợp lệ

    try:
        # Truy vấn danh sách chấm công từ bảng attendence
        sql = "SELECT id_attendence, id_employee, date, time_in, time_out FROM attendence"
        cursor.execute(sql)
        attendances = cursor.fetchall()

        # Chuyển đổi kết quả thành danh sách các từ điển (JSON serializable)
        attendance_list = []
        for attendance in attendances:
            attendance_data = {
                "id_attendence": attendance[0],
                "id_employee": attendance[1],
                "date": attendance[2].strftime("%Y-%m-%d") if attendance[2] is not None else None,
                "time_in": (attendance[3].strftime("%H:%M:%S") if isinstance(attendance[3], datetime) else str(attendance[3])) if attendance[3] is not None else None,
                "time_out": (attendance[4].strftime("%H:%M:%S") if isinstance(attendance[4], datetime) else str(attendance[4])) if attendance[4] is not None else None
            }
            attendance_list.append(attendance_data)

        return jsonify(attendance_list), 200  # Trả về danh sách chấm công và mã HTTP 200
    except Exception as e:
        print(f"Lỗi khi lấy danh sách chấm công: {e}")
        return jsonify({"error":"Something went wrong. Please try again later."}), 500

@app.route('/employees', methods=['GET'])
def get_employees():
    try:
        # Truy vấn danh sách nhân viên từ bảng employee
        sql = "SELECT id_employee, full_name FROM employee"
        cursor.execute(sql)
        employees = cursor.fetchall()

        # Chuyển đổi kết quả thành danh sách các từ điển (JSON serializable)
        employee_list = []
        for employee in employees:
            employee_data = {
                "id_employee": employee[0],
                "full_name": employee[1],
                "gender": employee[2],
                "email": employee[3],
                "phone": employee[4]
            }
            employee_list.append(employee_data)

        return jsonify(employee_list), 200  # Trả về danh sách nhân viên và mã HTTP 200
    except Exception as e:
        print(f"Lỗi khi lấy danh sách nhân viên: {e}")
        return jsonify({"error":"Something went wrong. Please try again later."}), 500
@app.route('/login', methods=['POST'])
def login():
    data = request.get_json()
    username = data.get('username')
    password = data.get('password')

    if not username or not password:
        return jsonify({"error": "Thiếu thông tin đăng nhập"}), 400  # Nếu thiếu thông tin

    try:
        # Kiểm tra thông tin đăng nhập trong cơ sở dữ liệu
        sql = "SELECT * FROM account WHERE username = %s AND password = %s"  # Thay đổi nếu bạn đã mã hóa mật khẩu
        cursor.execute(sql, (username, password))  # Thay đổi nếu sử dụng hash cho mật khẩu
        user = cursor.fetchone()

        if user:
            # Kiểm tra xem đã có auth key chưa
            sql = "SELECT key FROM api_keys WHERE username = %s"
            cursor.execute(sql, (username,))
            existing_key = cursor.fetchone()        
            return jsonify({"auth_key": API_KEY}), 200  # Trả về auth key
        else:
            return jsonify({"error":"Invalid API key."}), 401  # Nếu thông tin không hợp lệ
    except Exception as e:
        print(f"Lỗi khi đăng nhập: {e}")
        return jsonify({"error":"Something went wrong. Please try again later."}), 500
def save_faces_to_db(id_employee, faces_data):
    try:
        connection = connect_db()
        cursor = connection.cursor()

        # Lưu từng khuôn mặt vào cơ sở dữ liệu
        for face in faces_data:
            # Chuyển đổi từng khuôn mặt thành định dạng blob (binary)
            face_blob = pickle.dumps(face)
            
            # Chèn từng ảnh khuôn mặt vào MySQL
            sql = "INSERT INTO face (id_employee, face_data) VALUES (%s, %s)"
            cursor.execute(sql, (id_employee, face_blob))

        # Commit để lưu tất cả thay đổi
        connection.commit()

        # Đóng kết nối
        cursor.close()
        connection.close()

    except mysql.connector.Error as err:
        print("Error: {}".format(err))
        raise Exception("Lỗi khi lưu dữ liệu vào cơ sở dữ liệu MySQL")

# Khởi động webcam và thu thập dữ liệu khuôn mặt
def capture_face_data(user_id):
    # Kiểm tra xem webcam có mở thành công hay không
    if not video.isOpened():
        raise Exception("Không thể truy cập camera")

    faces_data = []
    i = 0

    while True:
        ret, frame = video.read()
        
        # Nếu không đọc được khung hình, thoát vòng lặp
        if not ret:
            raise Exception("Không thể đọc khung hình từ camera")

        gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
        faces = face_cascade.detectMultiScale(gray, 1.3, 5)

        for (x, y, w, h) in faces:
            crop_img = frame[y:y+h, x:x+w]
            resized_img = cv2.resize(crop_img, (128, 128))  # Kích thước phù hợp cho mô hình SVM
            if i % 10 == 0:
                faces_data.append(resized_img)
            i += 1

            # Vẽ hình chữ nhật quanh khuôn mặt
            cv2.rectangle(frame, (x, y), (x+w, y+h), (50, 50, 255), 1)

        # Thoát nếu đã thu thập đủ dữ liệu khuôn mặt hoặc người dùng nhấn 'q'
        if len(faces_data) >= 50:
            break



    # Lưu dữ liệu khuôn mặt
    faces_data = np.asarray(faces_data)
    faces_data = faces_data.reshape(faces_data.shape[0], -1)  # Chuyển đổi dữ liệu thành vector phẳng

    # Lưu ID và dữ liệu khuôn mặt vào cơ sở dữ liệu
    save_faces_to_db(user_id, faces_data)

# API để thu thập dữ liệu khuôn mặt bằng ID
@app.route('/collect_face', methods=['POST'])
def collect_face():
    # Lấy ID từ yêu cầu POST
    user_id = request.json.get('id')

    if not user_id:
        return jsonify({"error": "ID không được cung cấp"}), 400

    # Thu thập dữ liệu khuôn mặt
    try:
        capture_face_data(user_id)
        return jsonify({"message": "Dữ liệu khuôn mặt đã được thu thập cho ID {}".format(user_id)}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500

# API để kiểm tra xem dữ liệu đã lưu thành công chưa
@app.route('/status', methods=['GET'])
def status():
    # Kiểm tra xem tệp dữ liệu đã tồn tại chưa
    if os.path.exists(os.path.join(data_folder, 'faces_data.pkl')):
        return jsonify({"message": "Dữ liệu khuôn mặt đã được lưu trữ"}), 200
    else:
        return jsonify({"message": "Dữ liệu khuôn mặt chưa được lưu"}), 200

# API cung cấp video trực tiếp từ webcam
@app.route('/video_feed2')
def video_feed2():

    def generate_frames2():
        while True:
            success, frame = video.read()  # Đọc khung hình từ webcam
            if not success:
                break
            else:
                # Chuyển đổi khung hình từ BGR sang JPEG
                ret, buffer = cv2.imencode('.jpg', frame)
                frame = buffer.tobytes()  # Chuyển đổi về định dạng byte

                yield (b'--frame\r\n'
                       b'Content-Type: image/jpeg\r\n\r\n' + frame + b'\r\n')  # Gửi khung hình

    return Response(generate_frames2(), mimetype='multipart/x-mixed-replace; boundary=frame')

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
