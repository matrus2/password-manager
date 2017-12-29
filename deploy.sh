#!/bin/bash
set -ev
if [ "${TRAVIS_PULL_REQUEST}" = "false" ]; then
  docker-compose build
  $(aws ecr get-login --no-include-email --region eu-central-1)
  docker tag $EB_APP_NAME:latest $DOCKER_ECR
  docker push $DOCKER_ECR
  ts=`date +%s` && file_name="$EB_APP_NAME-$ts.zip"
  sed "s@IMAGE_NAME@${DOCKER_ECR}@g" Dockerrun.aws.json.template > Dockerrun.aws.json
  zip -r $file_name Dockerrun.aws.json
  aws s3 cp $file_name "s3://$S3_BUCKETNAME/$EB_APP_NAME/$file_name"
  cmsg=`git log -1 --pretty=format:"%s" | sed 's@\"@@g' | iconv -f utf-8 -t ascii | cut -c 1-200`
  aws elasticbeanstalk create-application-version --region eu-central-1 --application-name $EB_APP_NAME --version-label "$EB_APP_NAME-production-$ts" --description "$cmsg" --source-bundle S3Bucket="$S3_BUCKETNAME",S3Key="$EB_APP_NAME/$file_name" --no-auto-create-application
  aws elasticbeanstalk update-environment --region eu-central-1 --application-name $EB_APP_NAME --environment-name $EB_APP_ENV --version-label "$EB_APP_NAME-production-$ts"
fi