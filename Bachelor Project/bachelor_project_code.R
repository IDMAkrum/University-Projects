######### R Code for KNN and Random Forest on dataset ###########
######### Uses caret package, written by: I.D.M. Akrum ##########

# Install packages, necessary only once per computer
# install.packages('caret')
# install.packages('ISLR')

# Set working directory to where the data is 
setwd("/home/linux/OneDrive/Documents/Study/Bachelor Project")
library(ISLR)
library(caret)

vivago <- read.csv("FinalData.csv")

################## Data Processing ########################
vivago <- vivago[, -c(16:18)] # Remove patient information
vivago <- vivago[, -c(11:13)] # Remove Watchoff variable
vivago <- vivago[, -1] # Remove Day variable

# Remove hourly version of all variables
vivago <- vivago[, -3]
vivago <- vivago[, -7]
vivago <- na.omit(vivago)
head(vivago)

# Set train-test split
set.seed(400)
indxTrain <- createDataPartition(vivago$vivagoLabel, p = 2/3, list = FALSE)
training <- vivago[indxTrain,]
testing <- vivago[-indxTrain,]


# Checking the label distributions in the data sets
prop.table(table(training$vivagoLabel)) * 100
prop.table(table(testing$vivagoLabel)) * 100
prop.table(table(vivago$vivagoLabel)) * 100

# Train the KNN classifier
set.seed(400)
ctrl_knn <- trainControl(method="cv", number = 10)
knnFit <- train(as.factor(vivagoLabel) ~ ., data = training, method = "knn", trControl = ctrl_knn, preProcess = c("center","scale"), tuneLength = 20)
knnFit

plot(knnFit, type="b", main="KNN Accuracy", col="blue", xlab="#Neighbours", ylab = "Accuracy (10-Fold Cross-Validation)")

# Test the KNN classifier
set.seed(400)
knnPredict <- predict(knnFit, newdata = testing)
knn_cf <- confusionMatrix(knnPredict, as.factor(testing$vivagoLabel), positive = "0")
knn_cf 

################## Random Forest ########################
# Train the Random Forest classifier
set.seed(400)
ctrl_rf <- trainControl(method="oob")
rfFit <- train(as.factor(vivagoLabel) ~ ., data = training, method = "rf", trControl = ctrl_rf, preProcess = c("center","scale"), tuneLength = 20)
rfFit

plot(rfFit, type="b", main="RF Accuracy", xlab="#predictors", ylab = "Accuracy (Out-Of-Bag Estimate)")

# Test the Random Forest classifier
set.seed(400)
rfPredict <- predict(rfFit, newdata = testing)
rf_cf <- confusionMatrix(rfPredict, as.factor(testing$vivagoLabel), positive = "0")
rf_cf

############## Comparison With Other Labels #####################
# Set testing <- original vivago data + all labels
testing <- read.csv("FinalDataAllLabels.csv")

# Remove the rows where the watch is off
testing <- testing[testing$watchOffMinutes==0, ]
testing <- testing[,-c(20:22)] # remove patient information
testing <- testing[, -1] #remove the day variable

# Omit NAs from test data, then save the labels in their own vectors
testing <- na.omit(testing)
nurseLabels <- testing$nurseLabel
surveyLabels <- testing$surveyLabel

# Checking the label distributions in testing and label vectors (here we'll see a completely different distribution)
prop.table(table(testing$vivagoLabel)) * 100
prop.table(table(surveyLabels)) * 100
prop.table(table(nurseLabels)) * 100

# Check the difference between the labels without any classification
confusionMatrix(as.factor(testing$vivagoLabel), as.factor(nurseLabels), dnn = c("Vivago", "Nurse"))
confusionMatrix(as.factor(testing$vivagoLabel), as.factor(surveyLabels), dnn = c("Vivago", "Survey"))

# Predict on other labels with the Random Forest classifier
set.seed(400)
rfPredict2 <- predict(rfFit, newdata = testing)
rf_cf_survey <- confusionMatrix(rfPredict2, as.factor(surveyLabels), positive = "0")
rf_cf_mental <- confusionMatrix(rfPredict2, as.factor(testing$mentalLabel), positive = "0")
rf_cf_physical <- confusionMatrix(rfPredict2, as.factor(testing$physicalLabel), positive = "0")
rf_cf_nurse <- confusionMatrix(rfPredict2, as.factor(nurseLabels), positive = "0")

accuracies <- c(rf_cf_survey$overall[1], rf_cf_nurse$overall[1], rf_cf_mental$overall[1], rf_cf_physical$overall[1])
sensitivities <- c(rf_cf_survey$byClass[1], rf_cf_nurse$byClass[1], rf_cf_mental$byClass[1], rf_cf_physical$byClass[1])
specificities <- c(rf_cf_survey$byClass[2], rf_cf_nurse$byClass[2], rf_cf_mental$byClass[2], rf_cf_physical$byClass[2])
rf_labels_table <- matrix(c(accuracies,sensitivities,specificities),nrow = 4,ncol = 3,
                          dimnames = list(c("Survey", "Nurse", "Mental", "Physical"), c("Accuracy", "Sensitivity", "Specificity")))
