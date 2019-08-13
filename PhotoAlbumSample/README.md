# AWS Android SDK Tutorial - Auth, Storage and AppSync

This tutorial will walk you through the Photo Album sample step-by-step to explain and demonstrate how to use the APIs. The Photo Album sample app allows users to sign up, sign in and sign out using Auth with Cognito User Pool; add, delete, update and query albums using AppSync GraphQL; upload and download through S3 buckets using S3 TransferUtility.

## Prerequisites

To run this sample, you need the following:

- An IDE (Android Studio recommended)
- Amplify CLI to generate required AWS resources for Auth, Storage and API
- Add AWS dependencies to your project in gradle

## Setting up the Sample app

- Git clone the sample GitHub repo. The sample app root is PhotoAlbumSample.
- Add required AWS resources using Amplify CLI. You can do this either manually or automatically.
- Import the sample as a project in an IDE.

## AWS Amplify CLI

The AWS Amplify CLI is a toolchain which includes a robust feature set for simplifying mobile and web application development. 

* [Install the CLI](#install-the-cli)
* [Start building your app](https://aws-amplify.github.io/docs)

## Install the CLI

 - Requires Node.js® version 8.11.x or later

Install and configure the Amplify CLI as follows:

```bash
$ npm install -g @aws-amplify/cli
$ amplify configure
```

## How to add AWS resources using Amplify CLI?

Manual steps:

  * In command line, `cd <your-project-root>` to enter your project root.
  * Run `amplify configure` to configure the AWS access credentials, AWS Region and sets up a new AWS User Profile.
  * Run `amplify init` to initialize a new project, that is set up deployment resources in the cloud and prepares your project for Amplify.
  * Run `amplify add <category>` to Adds cloud features to your app. Here, we use `auth`, `storage` and `api` as `<category>`. You can add them one by one.
  * Run `amplify push` to provise cloud resources with the latest local developments.
  
Automatic steps: 

  * Run `git clone https://github.com/AaronZyLee/amplify-cli.git -b integtest --single-branch` to clone amplify-cli repo.
  * `cd amplify-cli` and run `npm run setup-dev` to build amplify-cli
  * `cd packages/amplify-ui-tests`
  * Run `npm run config <your-project-root> android <list: categories>`. `[list: categories]` includes `auth`, `storage` and `api`
  * Before you run adding api for your cloud, paste your own graphql schema file under `./schemas`, change the file name to `simple_model.graphql`
