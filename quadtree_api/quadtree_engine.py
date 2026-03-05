from PIL import Image, ImageDraw
import numpy as np

class QuadNode:
    def __init__(self, x, y, width, height, img_array):
        self.x = x
        self.y = y
        self.width = width
        self.height = height
        self.children = []
        
        # Extract the exact region of pixels for this quadrant
        region = img_array[y:y+height, x:x+width]
        
        # Calculate the mean RGB color and the Mean Squared Error (variance)
        if region.size > 0:
            self.mean_color = np.mean(region, axis=(0, 1))
            self.error = np.mean(np.square(region - self.mean_color))
        else:
            self.mean_color = np.array([0, 0, 0])
            self.error = 0

    def split(self, img_array):
        """Splits the current node into four equal sub-quadrants."""
        half_w = self.width // 2
        half_h = self.height // 2
        
        # Stop splitting if we are down to 1 pixel
        if half_w >= 1 and half_h >= 1:
            self.children.append(QuadNode(self.x, self.y, half_w, half_h, img_array))
            self.children.append(QuadNode(self.x + half_w, self.y, self.width - half_w, half_h, img_array))
            self.children.append(QuadNode(self.x, self.y + half_h, half_w, self.height - half_h, img_array))
            self.children.append(QuadNode(self.x + half_w, self.y + half_h, self.width - half_w, self.height - half_h, img_array))
            return True
        return False

def build_tree(node, img_array, threshold):
    """Recursively builds the tree based on color variance threshold."""
    # If the variance is higher than our threshold, it's too detailed! Split it.
    if node.error > threshold:
        if node.split(img_array):
            for child in node.children:
                build_tree(child, img_array, threshold)

def draw_tree(node, draw):
    """Recursively draws the final compressed image."""
    if not node.children:
        # It's a leaf node. Draw the block of average color.
        color = tuple(node.mean_color.astype(int))
        draw.rectangle([node.x, node.y, node.x + node.width, node.y + node.height], fill=color)
    else:
        # Keep digging down to the leaf nodes
        for child in node.children:
            draw_tree(child, draw)

# In quadtree_engine.py, update just the bottom function:
def process_image(input_path: str, output_path: str, threshold: int):
    print(f"Opening image: {input_path}")
    
    img = Image.open(input_path).convert('RGB')
    img_array = np.array(img)
    height, width, _ = img_array.shape
    
    print(f"Calculating spatial variance with threshold {threshold}...")
    
    root = QuadNode(0, 0, width, height, img_array)
    build_tree(root, img_array, threshold)
    
    out_img = Image.new('RGB', (width, height))
    draw = ImageDraw.Draw(out_img)
    draw_tree(root, draw)
    
    out_img.save(output_path)
    print(f"Successfully saved full-color compression to: {output_path}")