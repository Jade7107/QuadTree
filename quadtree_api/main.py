from fastapi import FastAPI, File, UploadFile, HTTPException, Form
from fastapi.responses import FileResponse
from pathlib import Path
import shutil

# Import the brain we just built!
from quadtree_engine import process_image

app = FastAPI(
    title="QuadTree Image API",
    description="A microservice for spatial image compression.",
    version="1.0.0"
)

# 1. Dynamically find your exact folders based on your structure
# This looks at where main.py is, and goes up one level to D:\QuadTreeProject
BASE_DIR = Path(__file__).resolve().parent.parent 

INPUT_DIR = BASE_DIR / "images" / "input"
OUTPUT_DIR = BASE_DIR / "outputs"

# Ensure the directories exist just in case
INPUT_DIR.mkdir(parents=True, exist_ok=True)
OUTPUT_DIR.mkdir(parents=True, exist_ok=True)

@app.post("/api/v1/process/")
async def process_image_endpoint(
    threshold: int = Form(...), 
    file: UploadFile = File(...)
):
    input_file_path = INPUT_DIR / file.filename
    output_file_name = f"processed_{threshold}_{file.filename}"
    output_file_path = OUTPUT_DIR / output_file_name

    with open(input_file_path, "wb") as buffer:
        shutil.copyfileobj(file.file, buffer)

    try:
        process_image(str(input_file_path), str(output_file_path), threshold)
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error processing image: {str(e)}")

    return FileResponse(output_file_path, media_type="image/png", filename=output_file_name)