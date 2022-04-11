import 'dart:async';

import 'package:flutter/material.dart';
import 'package:connectivity_plus/connectivity_plus.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        // This is the theme of your application.
        //
        // Try running your application with "flutter run". You'll see the
        // application has a blue toolbar. Then, without quitting the app, try
        // changing the primarySwatch below to Colors.green and then invoke
        // "hot reload" (press "r" in the console where you ran "flutter run",
        // or simply save your changes to "hot reload" in a Flutter IDE).
        // Notice that the counter didn't reset back to zero; the application
        // is not restarted.
        primarySwatch: Colors.blue,
      ),
      home: const MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({Key? key, required this.title}) : super(key: key);

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  int _counter = 0;
  late StreamSubscription<ConnectivityResult> _connectivitySubscription;

  StreamSubscription? _timerSubscription;
  List<String> myList = [];

  void check() async {
    var connectivityResult = await (Connectivity().onConnectivityChanged);
    print(connectivityResult.single.then((value) => value));
    if (connectivityResult == ConnectivityResult.mobile) {
      print(connectivityResult);
      // I am connected to a mobile network.
    } else if (connectivityResult == ConnectivityResult.wifi) {
      // I am connected to a wifi network.
      print(connectivityResult);
    }
  }

  // static const methodChannel = MethodChannel("myChannel");
  // static const stream =
  //  EventChannel('myChannel');
  //  openCamera()  {
  //   try {
  //    String value =  methodChannel.invokeMethod('open');
  //    print({'value': value});
  //  // return value;
  //   }
  //   on Exception catch (e){
  //
  //     throw("gfgfk");
  //   }
  // }
  //
  // void info(){
  //
  //   _timerSubscription = stream.receiveBroadcastStream().listen(openCamera());
  // }
  //
  static const stream =
      const EventChannel('com.yourcompany.eventchannelsample/stream');

  // Object _timer = {};
  //StreamSubscription? _timerSubscription;

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    _enableTimer();
  }


  void _enableTimer() {
    if (_timerSubscription == null) {
      _timerSubscription = stream.receiveBroadcastStream().listen(_updateTimer);
    }
  }

  void _disableTimer() {
    if (_timerSubscription != null) {
      _timerSubscription!.cancel();
      _timerSubscription = null;
    }
  }

  void _updateTimer(timer) {
    debugPrint("Timer $timer");
    setState(() => myList.add(timer));
  }

  @override
  Widget build(BuildContext context) {
    check();
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      // body: Center(
      //   child: Column(
      //     mainAxisAlignment: MainAxisAlignment.center,
      //     children: <Widget>[
      //       // const Text(
      //       //   'You have pushed the button this many times:',
      //       // ),
      //       // Text(
      //       //   '$_counter',
      //       //   style: Theme
      //       //       .of(context)
      //       //       .textTheme
      //       //       .headline4,
      //       // ),
      //     ],
      //   ),
      // ),

      body: ListView.builder(
          itemCount: myList.length,
          itemBuilder: (context, index) {
            if (myList.isEmpty) {
              return const Center(
                child: Text('No Data'),
              );
            }

            return Row(
              children: [

                Text("upSpeed: " +myList[index].split(',')[0].toString()),
               const SizedBox(width: 10,),
                Text("Down Speed: " + myList[index].split(',')[1].toString()),
              ],
            );
          }),

      floatingActionButton: FloatingActionButton(
        onPressed: _enableTimer,
        tooltip: 'Increment',
        child: const Icon(Icons.add),
      ),
    );
  }
}
