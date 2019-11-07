#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Mon Mar 25 12:13:32 2019

Auxilary functions for the human detection program. 

@author: Ivana Akrum
"""

# import packages
from __future__ import print_function
import constants

''' Count how many lines a file has '''
def countLines(filePath):
    lines = 0
    with open(filePath,'r') as file:
         for line in file:
             lines += 1
             
    return lines

''' Calculate the Intersection over Union for two bound boxes '''
def calcIOU(xA, yA, xB, yB, 
            xMin, yMin, xMax, yMax):
    # Determine the (x, y)-coodrinates of the intersection rectangle
    xA_int = max(xA, xMin)
    yA_int = max(yA, yMin)
    xB_int = min(xB, xMax)
    yB_int = min(yB, yMax)
    
    # Compute the area of intersection rectangle
    interArea = max(0, (xB_int - xA_int)) * max(0, (yB_int - yA_int))
    
    # Compute the area of both the prediction and ground-truth rectangles
    detectedBoxArea = (xB - xA + 1) * (yB - yA + 1)
    trueBoxArea = (xMax - xMin + 1) * (yMax - yMin + 1)
    
    # Compute the intersection over union by taking the intersection 
    # area and dividing it by the sum of prediction + ground_truth
    # areas - the intersection area
    iou = interArea/ float(detectedBoxArea + trueBoxArea - interArea)
        
    return iou
    
''' Display the results '''
def displayResults(metrics):
    tp = metrics[constants.TP]
    tn = metrics[constants.TN]
    fp = metrics[constants.FP]
    fn = metrics[constants.FN]
    
    accuracy = (tp + tn)/(tp + tn + fp + fn)
    tpr = tp/(tp+fn)
    fpr = fp/(fp+tn)
    
    print("\nTrue Positives: {}  False Positives: {}".format(tp, fp))
    print("True Negatives: {}  False Negatives: {}\n".format(tn, fn))
    print("Accuracy: {}%".format(accuracy*100))
    print("True Positive Rate: {}".format(tpr*100))
    print("False Positive Rate: {}".format(fpr*100))
    



    