import cv2
import numpy as np
import pickle
from sklearn.svm import SVC
from datetime import datetime
import os
from win32com.client import Dispatch
from datetime import datetime
import time
import csv
import mysql.connector


def speak(text):
    speaker = Dispatch("SAPI.SpVoice")
    speaker.Speak(text)


# Tải mô hình và bộ mã hóa nhãn
with open('D:/data/svm_model.pkl', 'rb') as f:
    svm_model = pickle.load(f)
with open('D:/data/label_encoder.pkl', 'rb') as f:
    label_encoder = pickle.load(f)

# Khởi động webcam
video = cv2.VideoCapture(0)
facedetect = cv2.CascadeClassifier(
    'D:/face/haarcascade_frontalface_default.xml')

# Ngưỡng xác suất để xác định "unknown"
prob_threshold = 0.75  # Bạn có thể điều chỉnh giá trị này


COL_NAMES = ['NAME', 'TIME', 'STATUS']
colors = {
    'green': (0, 255, 0),
    'red': (0, 0, 255),
}

# Kết nối với MySQL
connection = mysql.connector.connect(
    host="localhost",
    user="root",  # Thay bằng tên người dùng MySQL
    password="123456",  # Thay bằng mật khẩu MySQL
    database="user",
    port=3406
)
cursor = connection.cursor()

while True:
    ret, frame = video.read()
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
    faces = facedetect.detectMultiScale(gray, 1.3, 5)
    show = ""
    for (x, y, w, h) in faces:
        crop_img = frame[y:y+h, x:x+w, :]
        resized_img = cv2.resize(crop_img, (128, 128))
        resized_img = resized_img.flatten().reshape(1, -1)

        # Lấy xác suất dự đoán từ SVM
        probabilities = svm_model.predict_proba(resized_img)
        max_prob = np.max(probabilities)
        predicted_class = np.argmax(probabilities)

        if max_prob > prob_threshold:
            predicted_id = label_encoder.inverse_transform([predicted_class])[
                0]
            # Hiển thị khung được nhận diện
            cv2.rectangle(frame, (x, y), (x+w, y+h), colors['green'], 2)
            # Hiển thị tên người được nhận diện
            sql = "SELECT name FROM employee WHERE id_employee = %s"
            cursor.execute(sql, (str(predicted_id),))
            results = cursor.fetchall()
            row = results[0]
            name = row[0]
            show = str(name).upper() + "-" + str(predicted_id)
        else:
            # Hiển thị khung được nhận diện
            cv2.rectangle(frame, (x, y), (x+w, y+h), colors['red'], 2)
            show = "Unknown"

        cv2.putText(frame, show, (x, y-10),
                    cv2.FONT_HERSHEY_SIMPLEX, 1, (255, 255, 255), 2)

    cv2.imshow("Frame", frame)

    k = cv2.waitKey(1)
    ts = time.time()
    date = datetime.fromtimestamp(ts).strftime("%y-%m-%d")

    if show != "Unknown":
        # check in
        if k == ord('0'):
            speak(f"{str(show)} have checkin")

            timestamp = datetime.fromtimestamp(ts).strftime("%H:%M:%S")
            attendance = [str(predicted_id), str(timestamp)]
            time.sleep(1)

            # Ghi dữ liệu vào database
            check_checkin = "SELECT COUNT(*) FROM attendence WHERE id_employee=%s and date=%s "
            cursor.execute(check_checkin, (str(predicted_id), str(date)))
            results = cursor.fetchall()
            if results:
                row = results[0]
                count = row[0]

            if count == 0:  # nếu user chưa có lần checkin trong ngày
                sql = "INSERT INTO attendence(id_employee,date,time_in) VALUES (%s,%s,%s);"
                cursor.execute(
                    sql, (str(predicted_id), str(date), str(timestamp)))
                connection.commit()

        # check out
        if k == ord('1'):
            speak(f"{str(show)} have checkout")

            # Ghi dữ liệu vào database

            timestamp = datetime.fromtimestamp(ts).strftime("%H:%M:%S")
            attendance = [str(predicted_id), str(timestamp)]
            time.sleep(1)
            check_checkout = "SELECT COUNT(*) FROM attendence WHERE id_employee=%s and date =%s;"
            cursor.execute(check_checkout, (str(predicted_id), str(date)))
            results = cursor.fetchall()
            if results:
                row = results[0]
                count = row[0]
            if count == 1:  # Nếu người dùng đã checkin
                sql = "UPDATE attendence SET time_out=%s WHERE id_employee=%s and date =%s"
                cursor.execute(
                    sql, (str(timestamp), str(predicted_id), str(date)))
                connection.commit()

    if k == ord('q'):
        break
cursor.close()
connection.close()
video.release()
cv2.destroyAllWindows()
