/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 */

import React, {useEffect, useState} from 'react';
import type {PropsWithChildren} from 'react';
import {
  SafeAreaView,
  ScrollView,
  StatusBar,
  StyleSheet,
  Text,
  useColorScheme,
  View,
  Button,
  FlatList,
  ToastAndroid,
  PermissionsAndroid,
  Platform,
  ActivityIndicator
} from 'react-native';

import { Card } from '@rneui/base';

import {
  Colors,
  DebugInstructions,
  Header,
  LearnMoreLinks,
  ReloadInstructions,
} from 'react-native/Libraries/NewAppScreen';

import { NativeModules } from 'react-native';

type Material = {
    ItemCode: string,
    ItemPrice: string,
    NewPrice: string
}

type Materials = Material[];

var FTPNetwork = NativeModules.FTPNetwork;
var SQLite = NativeModules.SQLite;

type SectionProps = PropsWithChildren<{
  title: string;
}>;

async function downloadFromFTP() {
  FTPNetwork.downloadFile( (err: any) => {console.log(err)}, (msg: string) => {console.log(msg)} );
}

async function getMaterials() {
 SQLite.readMaterialsFromDB((err: any) => {console.log(err)}, (msg: string) => {
  const materials = JSON.parse(msg);
  return materials;
 }) 
}

function App(): JSX.Element {
  const isDarkMode = useColorScheme() === 'dark';
  const [ data, setData ] = useState<Materials>([]);
  const [ isLoading, seIsLoading ] = useState(true);

  useEffect(() => {
    downloadAndRefresh();
  }, []);

  const downloadAndRefresh = () => {
    FTPNetwork.downloadFile((err: any) => {console.log(err)}, (msg: string) => {
      ToastAndroid.showWithGravity("Download Successfull!", 10, ToastAndroid.CENTER);
      SQLite.readMaterialsFromDB((err: any) => {console.log(err)}, (msg: string) => {
        const materials = JSON.parse(msg);
        setData(materials);
        seIsLoading(false);
      });
    })
  }
  const backgroundStyle = {
    backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
  };

  return (
    <View style={styles.container}>
      <Text style={styles.item}>FTP App</Text>
      <ActivityIndicator style={styles.loader} animating={isLoading} color="black" />
      <FlatList
        data={data}
        renderItem={({item}) => <Card >
            <Text style={styles.item}>{item.ItemCode}</Text>
            <Text style={styles.item}>Price: {item.ItemPrice}</Text>
          </Card>}
      />
      <Text style={styles.item}>Data</Text>
      <Button title="Download & Refresh Data" onPress={downloadAndRefresh} />
  </View>
  );
}
const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    backgroundColor: '#ecf0f1',
    padding: 8,
  },
  item: {
    margin: 14,
    fontSize: 18,
    color: 'black',
    fontWeight: 'bold',
    textAlign: 'center',
  },
  loader: {
    alignSelf: 'center',
    flex: 1,
    tintColor: 'cyan'
  }
});

export default App;
