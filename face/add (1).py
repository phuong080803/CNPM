# -*- coding: utf-8 -*-
"""
Created on Fri Sep  6 20:37:05 2024

@author: ADMIN
"""

import cv2
import numpy as np
import pickle
import mysql.connector
from datetime import datetime
# Khởi động webcam
video = cv2.VideoCapture(0)
facedetect = cv2.CascadeClassifier(
    'D:/face/haarcascade_frontalface_default.xml')

faces_data = []
id_employee = input("Enter Your ID: ")
i = 0

while True:
    ret, frame = video.read()
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
    faces = facedetect.detectMultiScale(gray, 1.3, 5)

    for (x, y, w, h) in faces:
        crop_img = frame[y:y+h, x:x+w, :]
        # Kích thước lớn hơn cho SVM
        resized_img = cv2.resize(crop_img, (128, 128))
        if i % 10 == 0:
            faces_data.append(resized_img)
        i += 1
        cv2.putText(frame, str(len(faces_data)), (50, 50),
                    cv2.FONT_HERSHEY_COMPLEX, 1, (50, 50, 255), 1)
        cv2.rectangle(frame, (x, y), (x+w, y+h), (50, 50, 255), 1)

    cv2.imshow("Frame", frame)
    k = cv2.waitKey(1)
    if k == ord('q') or len(faces_data) >= 50:
        break

video.release()
cv2.destroyAllWindows()

# Lưu dữ liệu khuôn mặt
faces_data = np.asarray(faces_data)
# Chuyển thành dạng vector phẳng
faces_data = faces_data.reshape(faces_data.shape[0], -1)


# Kết nối với MySQL
connection = mysql.connector.connect(
    host="localhost",
    user="root",  # Thay bằng tên người dùng MySQL
    password="123456",  # Thay bằng mật khẩu MySQL
    database="user",
    port=3406
)

cursor = connection.cursor()
# Lưu dữ liệu khuôn mặt và tên vào MySQL
for face in faces_data:
    face_blob = pickle.dumps(face)  # Chuyển dữ liệu khuôn mặt thành blob
    sql = "INSERT INTO face(id_employee,face_data) VALUES (%s, %s)"
    cursor.execute(sql, (id_employee, face_blob))

# Commit để lưu thay đổi
connection.commit()

# Đóng kết nối
cursor.close()
connection.close()
