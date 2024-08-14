# Instructions on Running the OwlMaps Demo

### The following instructions are for *Windows* and *Linux* ONLY!! 
### For *MacOS*, please follow the instructions in Canvas!
 
To run the OwlMaps Demo from inside of Eclipse, a valid run/launch configuration is needed.  

There are two ways in which create an operational launch configuration for the demo:

## Method 1: Create your own launch configuration:

The easiest way to create your own launch config:
1. Right-click the `provided.owlMaps.demo.controller.DemoApp` class in Eclipse's Package Explorer and select `Run As/Java Application`.
2. The application will attempt to run but will fail -- that's ok because the point was to create an entry in the Run Configurations.
3. Open the `Run Configurations...` dialog (pull down the green arrow on the icon bar) and select the `DemoApp` configuration that was just made.
4. Set the working directory to be the project's `bin` folder.
5. Go the `Common` tab and select the `Shared File...` radio button.  By default, launch configurations are saved to the root of the project.
6. Click the `Apply` button to save the launch file.

Run the launch configuration by either clicking the `Run` button in the `Run Configurations` dialog or selecting the launch file in the Package Explorer and clicking the green Run icon.

_Note:_ If you change the project's module name, you will either need to edit the launch configuration file or re-make it so that it will use the new module name.

The advantage of this technique is that it creates a launch config that can be used by anyone with the same project name and same project module name, e.g. teammates sharing the repository.  The launch config is not universally usable.


## Method 2: Add a launch configuration "variable" and use the provided launch configuration

1. Try to run the provided launch config file by selecting the `provided.owlMaps.demo.OwlMaps_Demo.launch` file in the Package Explorer and clicking the green Run icon.   
2. The execution will fail, stating an unknown variable called "module_name" -- that's ok because the point was to create an entry in the  _External Tools_  Configurations.
3. Open the `External Tools Configurations...` dialog (pull down the green arrow  _with the red toolbox_  icon on the icon bar) and select the `OwlMaps_Demo` configuration.
4. Click any of the `Variables...` buttons shown on the dialog.
5. Click the `Edit Variables` button and then the `New...` button.
6. Enter the following information and then click `OK` to save the variable:


* Name = `module_name`   (no quotes)
* Value =  _[the name of your project's module from the module_info]_ 
* Description: _The project's module name_ -- i.e. something descriptive of what this variable is.

The launch variables are per-Eclipse installation, so  _every_  developer who wishes to use the provided launch config will need to set the `module_name` variable.  

Run the launch configuration by either clicking the `Run` button in the `External Tools Configurations` dialog or selecting the launch file in the Package Explorer and clicking the green Run icon.

_Note:_ If you change your module, you will need to update the `module_name` variable entry!  

The advantage of this technique is that it uses a universal launch config that anyone can use once they've set the `module_name` launch variable.
