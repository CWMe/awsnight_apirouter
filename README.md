Welcome to the Code With Me - AWS Night Demo application
==============================================

This project will demonstrate several things:

* Building and deploying an application with the AWS DevOps pipeline (GitHub -> CodePipeline -> CodeBuild -> Cloudformation -> Lambda)
* Developing a Lambda function
* Using additional AWS services (API Gateway, SNS, S3)

To use this application:

* Find a news article you want stripped of its HTML, CSS, JS, etc. and copy the URL

Use one of the following to query the API

* PostMan
* cURL

* Issue the following (cURL) command: 
curl -XPUT 'https://cb53qf4chb.execute-api.us-east-1.amazonaws.com/Prod/' -d '{"url":"http://www.cnn.com/2017/12/06/politics/donald-trump-israel-palestinians-jerusalem-politics/index.html"}'
(swapping out the URL for your own)