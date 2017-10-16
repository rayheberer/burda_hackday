# Burda Hackday

[Burda Hackday #5](http://burdahackday.de/) was a fitness-themed hackathon hosted by [Burda Bootcamp](http://burdabootcamp.de/) on the 7th and 8th of October, 2017.

Sachin Bakshi, Ray Heberer, and Adithya Saladi contributed to the design and development of a prototype Android application that used conditional generative adversarial networks to perform image-to-image translation, capturing the mapping from before to after photos in male body transformations. The demo went on to win 2nd place.

Being a prototype, the app lacks all functionality not directly related to what was shown during the pitch.

### Dataset

The small set of <150 paired photos is available at https://www.floydhub.com/rayheberer/datasets/dreamfit

### Model

The model used was [affinelayer's Tensorflow implementation of pix2pix](https://github.com/affinelayer/pix2pix-tensorflow), with only minor adjustments made to facilitate training on the cloud.