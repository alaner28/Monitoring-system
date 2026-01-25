# from ultralytics import YOLO
# # 加载模型
# model = YOLO(model="/root/yolov10-main/runs/detect/train62/weights/best.pt")
# # 导出onnx格式
# model.export(format="onnx")
from ultralytics import YOLO
 
# 加载模型
model = YOLO('/root/yolov10-main/yolov10n.pt')  # 加载官方模型（示例）
model = YOLO('/root/yolov10-main/runs/detect/train62/weights/best.pt')  # 加载自定义训练模型（示例）
 
# 导出模型
model.export(format='onnx')