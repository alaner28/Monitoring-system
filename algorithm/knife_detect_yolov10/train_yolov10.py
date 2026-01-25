from ultralytics import YOLOv10

model = YOLOv10()
# If you want to finetune the model with pretrained weights, you could load the 
# pretrained weights like below
# model = YOLOv10.from_pretrained('jameslahm/yolov10{n/s/m/b/l/x}')
# or
#wget https://github.com/THU-MIG/yolov10/releases/download/v1.1/yolov10n.pt
#model = YOLOv10('yolov10n.pt')

model.train(data='dataset/data.yaml', epochs=350, batch=128, imgsz=640,workers=8,cache=True,workspace=8)