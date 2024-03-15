import cv2
import numpy as np
import pyautogui
from PIL import ImageGrab


# Khởi tạo bộ phát hiện vật thể
object_detector = cv2.createBackgroundSubtractorMOG2()

def detect_and_track_object(frame, object_image):
    # Chuyển đổi khung hình sang không gian màu xám để dễ xử lý
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
    
    # Áp dụng bộ phát hiện vật thể để tách nền và vật thể
    mask = object_detector.apply(gray)
    
    # Áp dụng các bước xử lý để làm sạch và cải thiện kết quả
    blur = cv2.GaussianBlur(mask, (5, 5), 0)
    _, thresh = cv2.threshold(blur, 20, 255, cv2.THRESH_BINARY)
    dilated = cv2.dilate(thresh, None, iterations=3)
    
    # Tìm các contour (đường viền) của vật thể
    contours, _ = cv2.findContours(
        dilated, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
    
    # Đọc ảnh vật thể từ đường dẫn
    object_img = cv2.imread(object_image, cv2.IMREAD_GRAYSCALE)
    
    # Chỉ vẽ viền và ghi lại video nếu có ít nhất một contour được tìm thấy
    if len(contours) > 0:
        for contour in contours:
            # Lấy hình chữ nhật bao quanh contour
            (x, y, w, h) = cv2.boundingRect(contour)
            
            # So sánh vật thể tìm thấy với ảnh vật thể đầu vào
            object_match = cv2.matchTemplate(gray[y:y+h, x:x+w], object_img, cv2.TM_CCOEFF_NORMED)
            threshold = 0.8  # Ngưỡng độ tương đồng
            loc = np.where(object_match >= threshold)
            
            if len(loc[0]) > 0:
                # Vẽ hộp trắng bao quanh vật thể
                cv2.rectangle(frame, (x, y), (x+w, y+h), (255, 255, 255), 2)
    
    return frame

# Khởi tạo bộ ghi video
video_writer = cv2.VideoWriter('output.avi', 
                               cv2.VideoWriter_fourcc(*'MJPG'), 
                               30, 
                               (640, 480))

# Đường dẫn đến ảnh vật thể
object_image_path = r'C:\Users\Admin\Desktop\code\limbo key\key2.png'

# Bắt đầu quá trình theo dõi và ghi lại video trên màn hình
screen = np.array(ImageGrab.grab())
while True:
    # Sao chép màn hình để xử lý
    frame = screen.copy()
    
    # Gọi hàm detect_and_track_object để theo dõi và vẽ viền vật thể
    processed_frame = detect_and_track_object(frame, object_image_path)
    
    # Ghi frame đã xử lý vào video
    video_writer.write(processed_frame)
    
    # Hiển thị frame đã xử lý trong cửa sổ
    cv2.imshow('Object Tracking', processed_frame)
    
    # Thoát khỏi vòng lặp nếu nhấn phím 'q'
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

# Giải phóng bộ ghi video và cửa sổ hiển thị
video_writer.release()
cv2.destroyAllWindows()