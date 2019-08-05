from utility_functions import runcommand
import os
import json
from shutil import rmtree
from shutil import copyfile

def get_test_names_from_json(test_names_json_file):
    with open(test_names_json_file) as json_file:
        test_names = json.load(json_file)
        ui_test_names = []
        for test_name in test_names["tests"]:
            ui_test_names.append(test_name["auth"])
            ui_test_names.append(test_name["api_1"])
            ui_test_names.append(test_name["api_2"])
            ui_test_names.append(test_name["storage"])
    return ui_test_names

def build_and_uitest(circleci_root_directory, app_name, app_repo_root_directory):
    # make a directory to store test results
    uitest_results_directory = circleci_root_directory + '/uitest_android_results'
    try:
        if os.path.exists(uitest_results_directory):
            # if the directory already exists, delete it
            rmtree(uitest_results_directory)
        os.mkdir(uitest_results_directory)
        print('uitest_results directory is created.')
    except OSError as err:
        print('Cannot locate uitest_results_directory.\n')
        print(str(err))
        exit(1)

    # cd into app root directory
    app_root_directory = '{0}/{1}'.format(app_repo_root_directory, app_name)
    try:
        os.chdir(app_root_directory)
    except OSError as err:
        print('Can not locate app_root_directory.\n')
        print(str(err))
        exit(1)

    # build and run ui test
    print('Run UI Tests...')
    ui_tests = get_test_names_from_json("{0}/CircleCIScripts/test_names.json".format(app_repo_root_directory))
    for ui_test in ui_tests:
        run_uitest(ui_test)
        store_uitest_results(ui_test, app_root_directory, uitest_results_directory)

def run_uitest(ui_test):
    # command to run ui tests
    runcommand(command = "bash gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class"
                         "=com.amazonaws.android.samples.photosharing.{0}".format(ui_test))

def store_uitest_results(ui_test, app_root_directory, uitest_results_directory):
    try:
        filename = app_root_directory + '/app/build/reports/androidTests/connected/com.amazonaws.android.samples.photosharing.{0}.html'.format(ui_test)
        copyfile(filename, uitest_results_directory + '/com.amazonaws.android.samples.photosharing.{0}.html'.format(ui_test))
    except FileNotFoundError as err:
        print('Can not find file ' + filename + '\n')
        print(str(err))
        exit(1)
# build_and_uitest(circleci_root_directory='/Users/cxuam/Desktop', app_repo_root_directory='/Users/cxuam/aws-android-sdk-sample', app_name='PhotoAlbumSample')
