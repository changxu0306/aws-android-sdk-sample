"""
author: Phani Srikar Edupuganti
owner: com.amazonaws
"""


from utility_functions import runcommand
from uitests_exceptions import *
from shutil import rmtree, copyfile
import os

## Requirements: aws-cli, npm, yarn, git cli tools

def configure_aws_resources(app_repo_root_directory, appname):

    app_root_directory = "{0}/{1}".format(app_repo_root_directory, appname)
    cli_resources = ['auth', 'storage', 'api']

    pathToCliRepo = app_root_directory + '/configure-aws-resources/amplify-cli'

    try:
        if os.path.isdir(app_root_directory + '/configure-aws-resources'):
            rmtree(app_root_directory + '/configure-aws-resources')

        os.mkdir(app_root_directory + '/configure-aws-resources')
        os.chdir(app_root_directory + '/configure-aws-resources')
        if os.path.isdir(pathToCliRepo):
            rmtree(pathToCliRepo)

    except OSError as err:
        raise OSErrorConfigureResources(appname, [str(err)])

    print("step: 1/3... Cloning amplify-cli repo \n ")
    runcommand(command="git clone https://github.com/AaronZyLee/amplify-cli -b integtest",
               exception_to_raise = GitCloneCliException(appname))

    try:
        os.chdir(pathToCliRepo)
    except OSError as err:
        raise OSErrorConfigureResources(appname, [str(err)])

    print("step: 2/3... Run setup-dev \n ")
    runcommand(command = "npm run setup-dev",
               exception_to_raise = CliSetupDevException(appname))

    ## todo: change to take custom schema
    try:
        os.chdir(app_root_directory + '/configure-aws-resources/amplify-cli/packages/amplify-ui-tests')
        if 'api' in cli_resources:
            targetSchemaPath = app_root_directory + '/configure-aws-resources/amplify-cli/packages/amplify-ui-tests/schemas/simple_model.graphql'
            if os.path.exists(targetSchemaPath):
                os.remove(targetSchemaPath)
            copyfile(app_root_directory + '/simple_model.graphql', targetSchemaPath)
    except OSError as err:
        raise OSErrorConfigureResources(appname, [str(err)])

    print("step: 3/3... config resources \n ")
    configure_command = "npm run config {0} android {1}".format(app_root_directory, " ".join(cli_resources))

    runcommand(command = configure_command,
               exception_to_raise = CliConfigException(appname))
