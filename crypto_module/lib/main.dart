import 'dart:convert';

//import 'package:cryptoflutter/home_screen.dart';
import 'package:cryptomodule/home_screen.dart';
import 'package:flutter/material.dart';
//import 'package:http/http.dart' as http;
import 'package:http/http.dart' as http;

void main() async {
  List currencies = await getCurrencies();
  runApp(MyApp(currencies));
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  final List _currencies;
  MyApp(this._currencies);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      theme: ThemeData(
        primarySwatch: Colors.pink,
      ),
      home: HomeScreen(_currencies),
    );
  }
}

Future<List> getCurrencies() async {
  var queryParameters = {
    'start': '1',
    'limit': '50',
    'convert': 'USD',
  };
  var cryptoApiLink = Uri.https('pro-api.coinmarketcap.com',
      '/v1/cryptocurrency/listings/latest', queryParameters);
  // http.Response response = await http.get(cryptoApiLink);
  // return jsonDecode(response.body);
  final http.Response response = await http.get(
    cryptoApiLink,
    headers: <String, String>{
      'Accept': 'application/json',
      'X-CMC_PRO_API_KEY': "32259241-51c1-4e3a-8929-05358d0f8e7c",
    },
    // body: jsonEncode(<String, dynamic>{
    //   'start': '1',
    //   'limit': '50',
    //   'convert': 'USD',
    // }),
    // encoding: Encoding.getByName('utf-8'),
  );

  if (response.statusCode == 200) {
    // var data =
//      print('non decoded' + response.body);
//      print('response print' + jsonDecode(response.body)['data'][0]['name']);
    return jsonDecode(response.body)['data'];
    // print(data);

  } else {
    var data = jsonDecode(response.body);
    // return data;
    print(data);
  }
  return jsonDecode(response.body)['data'];
}
