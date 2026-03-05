import streamlit as st
import requests
from PIL import Image
import io

# Setup the page aesthetics
st.set_page_config(page_title="QuadTree Image Compression", layout="wide")
st.title("🌳 Spatial Image Compression Engine")
st.markdown("Upload an image and adjust the variance threshold to see the QuadTree algorithm compress it into blocky, mathematical pixel-art in real-time.")

# The Slider!
threshold = st.slider(
    "Compression Threshold (Variance)", 
    min_value=100, 
    max_value=5000, 
    value=1500, 
    step=100,
    help="Lower = High Detail (Splits more often). Higher = High Compression (Giant blocks)."
)

uploaded_file = st.file_uploader("Choose an image...", type=["jpg", "jpeg", "png"])

if uploaded_file is not None:
    col1, col2 = st.columns(2)
    
    with col1:
        st.subheader("Original Image")
        st.image(uploaded_file, use_container_width=True)
        
    with col2:
        st.subheader("QuadTree Compressed Image")
        
        if st.button("Run Compression Engine", type="primary"):
            with st.spinner("Processing multidimensional arrays..."):
                # Send the image and slider value to your FastAPI backend
                files = {"file": (uploaded_file.name, uploaded_file.getvalue(), uploaded_file.type)}
                data = {"threshold": threshold}
                
                # Hit our local API
                response = requests.post("http://127.0.0.1:8000/api/v1/process/", files=files, data=data)
                
                if response.status_code == 200:
                    # Display the returned image!
                    processed_image = Image.open(io.BytesIO(response.content))
                    st.image(processed_image, use_container_width=True)
                    st.success(f"Successfully compressed with a variance threshold of {threshold}!")
                else:
                    st.error(f"Backend Error: {response.text}")