# PlantAI
PlantAI is an Android application. It is used for Detection of plant diseases and generte the pdf report of the disease.

FEATURES:- 
1. User Authentication: It uses Firebase authentication system by using E-mail and Password or by Google Authorization.
2. Disease Prediction: First we will trained the dataset using Tenson flow and then integrate in to the Java (Android) Application. It will take the image and process it according to tflite model. After that it generate the pdf using canva feature in Java, it contain the Desease and their Accuracy Level.
3. Fertilizer Calculation: It uses some mathematical algorithm to calculate the fertilizer Ratio. Basically it takes the Nitrogen, Phosphorus, Potassium value to calculate it.
4. Whether forecast: This is general feature for persons to fetch whether data, using location (first calculate latitude and longitude) using API openwhether.

TECHNOLOGY STACK:-

User Interface:- XML
Backend: Java
