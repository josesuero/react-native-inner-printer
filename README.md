
# react-native-inner-printer

## Getting started

`$ npm install react-native-inner-printer --save`

### Mostly automatic installation

`$ react-native link react-native-inner-printer`

### Manual installation


#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.mstn.rnprinter.RNInnerPrinterPackage;` to the imports at the top of the file
  - Add `new RNInnerPrinterPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-inner-printer'
  	project(':react-native-inner-printer').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-inner-printer/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-inner-printer')
  	```


## Usage
```javascript
import RNInnerPrinter from 'react-native-inner-printer';

// TODO: What to do with the module?
RNInnerPrinter;
```
  