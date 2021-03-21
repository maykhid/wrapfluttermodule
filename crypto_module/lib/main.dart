import 'dart:convert';

import 'package:cryptomodule/crypto_helper/crypto_helper.dart';
import 'package:cryptomodule/essential_strings.dart';
import 'package:cryptomodule/home_screen.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      theme: ThemeData(
        primarySwatch: Colors.pink,
      ),
      home: HomeScreen(),
    );
  }
}
// get crypto currencies
Future<List<CryptoHelper>> getCurrencies() async {
  var queryParameters = {
    'start': '1',
    'limit': '50',
    'convert': 'USD',
  };
  var cryptoApiLink = Uri.https(
      EssentialStrings.base_url,
      EssentialStrings.listings_endpoint,
      queryParameters);

  final http.Response response = await http.get(
    cryptoApiLink,
    headers: <String, String>{
      'Accept': 'application/json',
      'X-CMC_PRO_API_KEY': EssentialStrings.key,
    },
  );

  if (response.statusCode == 200) {
    // create new variable type List and assign response data to it
    List list = jsonDecode(response.body)['data'];

    // perform _constructList() on another thread and return its result as a return value for getCurrencies()
    // this was done to reduce "janking"
    return compute(_constructList, list);
  } else {
    throw Exception('Error on getting data!');
  }
}

// Constructing the expected result
Future<List<CryptoHelper>> _constructList(List list) async{
  // Extract the id's from the list with listModifier() and convert to string
  String s = _listModifier(list);

  // pass the id's string into getCryptoInfo() and call
  List cryptoInfoList = await getCryptoInfo(s, list);

  // this loop was created for the sole purpose of manipulatively
  // getting the logo

  List<CryptoHelper> _constructList = List();
  for (int i = 0; i < list.length; i++) {
    _constructList.add(
        new CryptoHelper(
            list[i]['id'],
            list[i]['name'],
            _checkInt(list[i]['quote']['USD']['price']),
            _checkInt(list[i]['quote']['USD']['percent_change_1h']),
            cryptoInfoList[i]));
    print('id ${list[i]['id']}, name ${list[i]['name']} price ${list[i]['quote']['USD']['price']}, url ${cryptoInfoList[i]}');
    print(_constructList[i].name);
  }
  return _constructList;
}

// get crypto info
Future<List> getCryptoInfo(String cryptIDS, List len) async {
  var queryParameter = {
    'id' : cryptIDS
  };
  var cryptoApiLink = Uri.https(
      EssentialStrings.base_url,
      EssentialStrings.info_endpoint,
      queryParameter);

  final http.Response response = await http.get(
    cryptoApiLink,
    headers: <String, String>{
      'Accept': 'application/json',
      'X-CMC_PRO_API_KEY': EssentialStrings.key,
    },
  );

  if(response.statusCode == 200){
    // print("The logo ${jsonDecode(response.body)['data']}");
    List _tempList = [];

    for (int i = 0; i < len.length; i++ ) {
      Map mapList = len[i];
      _tempList.add(jsonDecode(response.body)['data']["${mapList['id']}"]["logo"]);
    }

    return _tempList;
  }
  else throw Exception('Error on getting data!');
}

// extracts id from list and appends with commas
String _listModifier(List obj) {
  List _tempList = [];
  // create a loop to sort out id's into string
  for (int i = 0; i < obj.length; i++) {
    _tempList.add(obj[i]['id']);
  }
  String s = _tempList.join(',');
  print('curious  $s');
  return s;
}

// converts int to a double
double _checkInt(var val) {
  if(val is int) return val.roundToDouble();
  else return val as double;
}