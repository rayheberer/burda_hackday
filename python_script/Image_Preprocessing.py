import glob, os
from PIL import Image, ImageOps
def rename_images(directory, label):
    for ii, img in enumerate(glob.iglob(os.path.join(directory, '*.jpg'))):
        os.rename(img, os.path.join(directory, label + str(ii)+'.jpg'))


def split_image(img_dir, img_name, left_dir, right_dir):
    im = Image.open(os.path.join(img_dir, img_name))
    left = im.crop((0, 0, im.width/2, im.height))
    left.save(os.path.join(left_dir, img_name))
    right = im.crop((im.width/2, 0, im.width, im.height))
    right.save(os.path.join(right_dir, img_name))
    
def split_all_images(img_dir, left_dir, right_dir):
    for img in glob.iglob(os.path.join(img_dir, '*.jpg')):
        title = os.path.basename(img)
        split_image(img_dir, title, left_dir, right_dir)

def reflect_y(img_dir, img_name, label, out_dir, reflect):
    im = Image.open(os.path.join(img_dir, img_name))
    
    if reflect:
        im = ImageOps.mirror(im)
    
    name, ext = os.path.splitext(img_name)
    im.save(os.path.join(out_dir, name + label + ext))
       
def reflect_permutations(img_dir, out_dir, label, reflect=True):
    for img in glob.iglob(os.path.join(img_dir, '*.jpg')):
        title = os.path.basename(img)
        reflect_y(img_dir, title, label, out_dir, reflect)

def crop_top_bottom(img_dir, img_name, label, out_dir, upper_crop_fraction):
    im = Image.open(os.path.join(img_dir, img_name))
    height = im.height
    width = im.width
    diff = height - width
    cropped = im.crop((0, diff * upper_crop_fraction, width, height - diff*(1 - upper_crop_fraction)))
    
    name, ext = os.path.splitext(img_name)
    cropped.save(os.path.join(out_dir, name + label + ext))

def crop_all_images(img_dir, out_dir, label, upper_crop_fraction):
    for img in glob.iglob(os.path.join(img_dir, '*.jpg')):
        title = os.path.basename(img)
        crop_top_bottom(img_dir, title, label, out_dir, upper_crop_fraction)

rename_images(r'C:\Users\8050116\Documents\DPS\Events\Burda Hackathon\pix2pix-tensorflow\data\original', 'male')

split_all_images(r'C:\Users\8050116\Documents\DPS\Events\Burda Hackathon\pix2pix-tensorflow\data\original', 
                 r'C:\Users\8050116\Documents\DPS\Events\Burda Hackathon\pix2pix-tensorflow\data\split\left',
                 r'C:\Users\8050116\Documents\DPS\Events\Burda Hackathon\pix2pix-tensorflow\data\split\right')

labels = ['r0', 'r1']
directions = ['left', 'right']
reflects = [(False, False), (True, True)]
for ii, label in enumerate(labels):
    reflect_permutations(os.path.join(r'C:\Users\8050116\Documents\DPS\Events\Burda Hackathon\pix2pix-tensorflow\data\split', 'left'),
                         os.path.join(r'C:\Users\8050116\Documents\DPS\Events\Burda Hackathon\pix2pix-tensorflow\data\reflected', 'left'),
                         label, reflects[ii][0])
    reflect_permutations(os.path.join(r'C:\Users\8050116\Documents\DPS\Events\Burda Hackathon\pix2pix-tensorflow\data\split', 'right'),
                         os.path.join(r'C:\Users\8050116\Documents\DPS\Events\Burda Hackathon\pix2pix-tensorflow\data\reflected', 'right'),
                         label, reflects[ii][1])

fractions = [0, 0.5]
labels = ['a', 'b']
directions = ['left', 'right']
for crop_fraction, label in zip(fractions, labels):
    for side in directions:
        crop_all_images(os.path.join(r'C:\Users\8050116\Documents\DPS\Events\Burda Hackathon\pix2pix-tensorflow\data\reflected', side),
                        os.path.join(r'C:\Users\8050116\Documents\DPS\Events\Burda Hackathon\pix2pix-tensorflow\data\cropped', side),
                        label,
                        crop_fraction)

