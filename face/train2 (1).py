# -*- coding: utf-8 -*-
"""
Created on Thu Sep 19 08:02:34 2024

@author: ADMIN
"""

import numpy as np
import pickle
import mysql.connector
from sklearn.svm import SVC
from sklearn.preprocessing import LabelEncoder
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score


def fetch_training_data():
    # Kết nối MySQL
    connection = mysql.connector.connect(
        host="localhost",
        user="root",  # Thay bằng tên người dùng MySQL
        password="123456",  # Thay bằng mật khẩu MySQL
        database="user",
        port=3406
    )
    cursor = connection.cursor()

    # Lấy tất cả dữ liệu từ bảng training_data
    cursor.execute("SELECT face_data, id_employee FROM face")
    results = cursor.fetchall()

    X = []
    y = []

    for row in results:
        image_data = pickle.loads(row[0])
        label = row[1]
        # Giả sử dữ liệu là hình ảnh, chuyển đổi từ nhị phân sang định dạng mà mô hình có thể sử dụng

        X.append(image_data)
        y.append(label)

    # Đóng kết nối
    cursor.close()
    connection.close()

    return np.array(X), np.array(y)


def train_model():

    faces, labels = fetch_training_data()
    faces = np.asarray(faces)
    faces = faces.reshape(faces.shape[0], -1)
    label_encoder = LabelEncoder()
    labels = label_encoder.fit_transform(labels)

    # Chia dữ liệu thành tập huấn luyện và kiểm tra
    X_train, X_test, y_train, y_test = train_test_split(
        faces, labels, test_size=0.2, random_state=42)

    # Xây dựng mô hình SVC
    svm_model = SVC(kernel='linear', probability=True)
    svm_model.fit(X_train, y_train)

    # Đánh giá mô hình
    y_pred = svm_model.predict(X_test)
    accuracy = accuracy_score(y_test, y_pred)
    print(f"Độ chính xác của mô hình: {accuracy * 100:.2f}%")

    # Lưu mô hình và bộ mã hóa nhãn
    with open('D:/data/svm_model.pkl', 'wb') as f:
        pickle.dump(svm_model, f)
    with open('D:/data/label_encoder.pkl', 'wb') as f:
        pickle.dump(label_encoder, f)


train_model()
