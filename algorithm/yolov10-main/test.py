from ultralytics import YOLOv10
import supervision as sv
import cv2
import os

# 定义分类标签
classes = {0: 'person'}

# 加载模型
model = YOLOv10('runs/detect/train_yolov10b/weights/best.pt')

# 读取图像
image_path = 'image_00044.jpg'
image = cv2.imread(image_path)

# 进行预测
results = model(source=image, conf=0.25, verbose=False)[0]
detections = sv.Detections.from_ultralytics(results)

# 使用标注器
bounding_box_annotator = sv.BoundingBoxAnnotator()
label_annotator = sv.LabelAnnotator()

# 生成标签
labels = [
    f"{classes[class_id]} {confidence:.2f}"
    for class_id, confidence in zip(detections.class_id, detections.confidence)
]

# 标注图像
annotated_image = bounding_box_annotator.annotate(
    image.copy(), detections=detections
)
annotated_image = label_annotator.annotate(
    annotated_image, detections=detections, labels=labels
)

# 计算人数和密度
num_people = len([class_id for class_id in detections.class_id if class_id == 0])  # 统计识别到的person数量
area_m2 = 50  # 摄像头区域的面积
density_per_m2 = num_people / area_m2  # 计算密度（每平方米人数）

# 定义密度等级函数
def get_density_level(density_per_m2):
    if density_per_m2 < 0.83:
        return "Very Low"
    elif density_per_m2 < 1.53:
        return "Low"
    elif density_per_m2 < 2.5:
        return "Medium"
    elif density_per_m2 < 3.3:
        return "High"
    elif density_per_m2 < 5.5:
        return "Very High"
    else:
        return "Extremely High"


# 获取密度等级
density_level = get_density_level(density_per_m2)

# 在图像上显示人数、密度和密度等级（红色显示）
text = f"People: {num_people}, Density: {density_per_m2:.2f}, Level: {density_level}"
cv2.putText(annotated_image, text, (10, 30), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 0, 255), 2)  # 红色文本

# 获取图像的文件名和目录
directory, filename = os.path.split(image_path)
output_path = os.path.join(directory, f"annotated_{filename}")

# 保存标注后的图像
cv2.imwrite(output_path, annotated_image)
print(f"Annotated image saved as {output_path}")
