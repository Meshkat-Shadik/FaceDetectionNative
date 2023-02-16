import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class ListItem extends StatelessWidget {
  final String assetImagePath;
  const ListItem({
    super.key,
    required this.assetImagePath,
  });

  @override
  Widget build(BuildContext context) {
    return Column(
      children: <Widget>[
        Image.asset(
          assetImagePath,
          width: 200,
        ),
        ElevatedButton(
          onPressed: () async {
            const methodChannel = MethodChannel("com.example.method_channel");
            final uintImage = (await rootBundle.load(assetImagePath)).buffer;
            final base64Img = base64Encode(uintImage.asUint8List());
            var result = await methodChannel.invokeMethod("check", {
              "image": base64Img,
            });
            // create a snackbar
            final snackBar = SnackBar(
              content: Text(result),
            );
            ScaffoldMessenger.of(context).showSnackBar(snackBar);
          },
          child: const Text("Click Me"),
        ),
      ],
    );
  }
}
