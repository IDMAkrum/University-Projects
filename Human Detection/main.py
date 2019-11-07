#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Mon Mar 25 12:13:32 2019

Program that detects humans in a scene. A bounding box is drawn around any
detected humans. Returns True Positives, False Positives, True Negatives,
and False Negatives. As well as, True Positive Rate, False Positive Rate,
and Accuracy.

Link to dataset used: 
    https://drive.google.com/open?id=1tgq3UOqJmr0FVUTNCzlXZnGvNsvIWsvd

To run the program, make sure the dataset is available in the same folder
under the name "Data".

Call with --v to display images with the detected bounding boxes.
Call with --v+ to display images with true and detected bounding boxes. 

@author: Ivana Akrum
"""

# import packages
from __future__ import print_function
import sys
import os
import glob
from imutils.object_detection import non_max_suppression
import numpy as np
import imutils
import cv2
import aux_functions
import constants

''' Run HOG descriptor + Linear SVM with an image file '''
def runHOG(imagePath, annotationPath, metrics, pos):
    # initialise the HOG descriptor/person detector
    hog = cv2.HOGDescriptor()
    hog.setSVMDetector(cv2.HOGDescriptor_getDefaultPeopleDetector())
    # load the image 
    image = cv2.imread(imagePath)
    image_name = imagePath[imagePath.rfind("/") + 1:]
    orig = image.copy()
    if pos:
        annotation_name = image_name[0: image_name.rfind('.')]
        annotation_name = annotation_name + '.txt' 
        annotation = os.path.join(annotationPath, annotation_name)
    else:
        # resize negative image for faster and more accurate detection
        image = imutils.resize(image, width=min(180, image.shape[1]))
    
    # detect people in the image
    (rects, weights) = hog.detectMultiScale(image, winStride=(5, 5),                                             
                                            padding=(8, 8), scale=1.05)
    
    # draw the original bounding boxes
    for(x, y, w, h) in rects:
        cv2.rectangle(orig, (x, y), (x + w, y + h), (0, 0, 255), 2)
        
    # apply non-maxima suppression to the bounding boxes using a
    # fairly large overlap threshold to try to maintain overlapping
    # boxes that are still people
    rects = np.array([[x, y, x + w, y + h] for (x, y, w, h) in rects])
    pick = non_max_suppression(rects, probs=None, overlapThresh=0.65)
    
    # initialise the matches found for positive images
    matched = []
    if pos:
        true_boxes = aux_functions.countLines(annotation)
    else:
        match = True  # negative we assume no picks are found
        true_boxes = 0
        
    # draw the final bounding boxes
    for (xA, yA, xB, yB) in pick:
        if pos:
            # Assume pick does not match a true bounding box
            # and check it against annotations
            match = checkAnnotation(image, annotation, False, matched, metrics,
                                    xA, yA, xB, yB)
        # A bounding box was detected where there should be none
        elif not pos and len(pick) > 0:
            match = False
            metrics[constants.FP] += 1 
        
        # A bounding box detected something other than a human as a human
        if pos and match == False: 
            metrics[constants.FP] += 1
            
        # draw bounding box
        if SHOW_ALL:
            cv2.rectangle(image, (xA, yA), (xB, yB), (0, 0, 255), 2)
        else:
            cv2.rectangle(image, (xA, yA), (xB, yB), (0, 255, 0), 2)
        
    if pos and true_boxes > len(matched):
        # the image has humans that went undetected
        metrics[constants.FN] += (true_boxes - len(matched))
    elif not pos and match == True:
        # No boxes were detected and the image is negative
        metrics[constants.TN] += 1
        
    # show some information on the number of bounding boxes
    print("[INFO] {} : ".format(image_name))
    print("There are {} true boxes to detect of which {} were detected".format(
            true_boxes, len(matched)))
    print("Detected {} objects where there were none".format(
            len(pick) - len(matched)))
    print("Current metrics: TP = {}, FP = {}, TN = {}, FN = {}".format(
            metrics[0], metrics[1], metrics[2], metrics[3]))
    
    # show the output images
    if SHOW_BOXES:
        cv2.imshow("Detection After NMS", image)
        cv2.waitKey(0)
            
''' Check the annotations for the true coordinates of the bounded boxes,
    given that the image has humans to detect '''
def checkAnnotation(image, annotationPath, match, matched, metrics,
                    xA, yA, xB, yB):
    with open(annotationPath,'r') as file:
     for line in file:
        xMin, yMin, xMax, yMax = line.split(', ')
        coordinates = (xMin, yMin, xMax, yMax)
        
        # Enlarge true boxes to better fit the detected boxes
        yMax = int(yMax) * 1.1
        xMax = int(xMax) * 1.1
        yMin = int(yMin) * 0.9
        xMin = int(xMin) * 0.9
        
        # Check if the coordinates detected and the true coordinates 
        # match using Intersection over Union
        iou = aux_functions.calcIOU(int(xA), int(yA), int(xB), int(yB),
                   int(xMin), int(yMin), int(xMax), int(yMax))
        if iou > float(0.49):  
            # check that these coordinates haven't already been found
            if coordinates not in matched:
                match = True
                metrics[constants.TP] += 1
                matched.append(coordinates)  # add to found coordinates
                # draw true bounding boxes 
                if SHOW_ALL:
                    cv2.rectangle(image, (int(xMin), int(yMin)), 
                              (int(xMax), int(yMax)), (0, 255, 0), 2)
                break
    return match
        
wd = './Data'

SHOW_BOXES = False
SHOW_ALL = False

# show the output image
if len(sys.argv) > 1:
    if sys.argv[1] == '--v':
        SHOW_BOXES = True # with detected bounding boxes
        SHOW_ALL = False
    elif sys.argv[1] == '--v+':
        SHOW_BOXES = True
        SHOW_ALL = True  # with detected and true bounding boxes

# initialise the counters for perfomance metrics = [tp, fp, tn, fn]
metrics = [0, 0, 0, 0]

# initialise the various paths
posImagePath = os.path.join(wd, 'Easy')
negImagePath = os.path.join(wd, 'Neg')
annotations = os.path.join(wd, 'annotations')
pos_images = glob.glob(posImagePath + '/*')
neg_images = glob.glob(negImagePath + '/*')

# loop over the postive samples
for image in pos_images:
    runHOG(image, annotations, metrics, True)

# loop over the negative samples
for image in neg_images:
    runHOG(image, annotations, metrics, False)
    
# Calculate and display the performance metrics   
aux_functions.displayResults(metrics)




    
