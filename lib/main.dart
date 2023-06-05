import 'package:flutter/material.dart';
import 'package:mlkit_method_channel_test/list_body.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: const MyHomePage(),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key});
  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Flutter Demo Home Page'),
      ),
      body: Center(
        child: ListView(
          children: const [
            ListItem(assetImagePath: "assets/one.png"),
            ListItem(assetImagePath: "assets/two.png"),
            ListItem(assetImagePath: "assets/four.png"),
            ListItem(assetImagePath: "assets/five.png"),
          ],
        ),
      ),
    );
  }
}
