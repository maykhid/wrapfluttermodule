import 'package:cryptomodule/crypto_helper/crypto_helper.dart';
import 'package:cryptomodule/main.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class HomeScreen extends StatefulWidget {
  final List<CryptoHelper> currencies;
  HomeScreen(this.currencies);
  @override
  _HomeScreenState createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  @override
  void initState() {
    super.initState();
    // ignore
    // _getBatteryLevel();
  }
  final List<MaterialColor> _colors = [Colors.blue, Colors.indigo, Colors.red];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.black,
        elevation: 0.0,
        title: Center(
          child: Text("Crypto World"),
        ),
      ),
      body: _cryptoWidget(),
    );
  }

  Widget _cryptoWidget() {
    int lengthOfList = widget.currencies.length;
    print('length of List $lengthOfList');
    return Column(
      children: [
        Flexible(
          child: ListView.builder(
            itemCount: lengthOfList,
            itemBuilder: (BuildContext context, int index) {
              final List<CryptoHelper> currency = widget.currencies;
              final MaterialColor color = _colors[index % _colors.length];
              return _getListItemUI(currency, color, index);
            },
            physics: BouncingScrollPhysics(),
          ),
        ),
// ignore
//         Container(
//           child: RaisedButton(
//             child: Text(_batteryLevel),
//            onPressed: _getBatteryLevel,
//           ),
//         ),
      ],
    );
  }

  ListTile _getListItemUI(List<CryptoHelper> currency, MaterialColor color, int index) {
    return ListTile(
      leading: CircleAvatar(
        // backgroundColor: color,
        // child: Text(currency[index].name[0]),
        backgroundImage: NetworkImage(currency[index].logoUrl),
      ),
      title: Text(
        currency[index].name,
        style: TextStyle(fontWeight: FontWeight.bold),
      ),
      subtitle: _getSubtitle(currency[index].price,
          currency[index].percentageChange_1h),
      isThreeLine: true,
    );
  }

  Widget _getSubtitle(double priceUSD, double percentageChange) {
    TextSpan priceTextWidget = TextSpan(
        text: "\$${priceUSD.toStringAsFixed(2)}",
        style: TextStyle(color: Colors.black));
    String percentageChangeText =
        "      1 hour: ${percentageChange.toStringAsFixed(2)}";
    TextSpan percentageChangeTextWidget;

    if ((percentageChange) > 0) {
      percentageChangeTextWidget = TextSpan(
        text: percentageChangeText,
        style: TextStyle(color: Colors.green),
      );
    } else {
      percentageChangeTextWidget = TextSpan(
        text: percentageChangeText,
        style: TextStyle(color: Colors.red),
      );
    }
    return RichText(
      text: TextSpan(children: [priceTextWidget, percentageChangeTextWidget]),
    );
  }
}




//ignore
// static const platform = const MethodChannel('samples.flutter.dev/battery');

// String _batteryLevel = 'Unknown battery level';

// The _getBatteryLevel communicates with the platform channel to get battery level
// of the devices

// Future<void> _getBatteryLevel() async {
//   String batteryLevel;
//    String text = 'pass value to native code';
//    List currencyData = widget.currencies;

//   try {
//     final int result = await platform.invokeMethod('getBatteryLevel');
//     batteryLevel = 'Battery level at $result % .';
//   } on PlatformException catch (e) {
//     batteryLevel = "Failed to get battery level: '${e.message}'.";
//   }
//
//   setState(() {
//     _batteryLevel = batteryLevel;
//     print('This is the ba3 level from flutter $_batteryLevel');
//   });
// }
