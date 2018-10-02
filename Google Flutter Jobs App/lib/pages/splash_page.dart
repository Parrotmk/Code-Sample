import 'package:flutter/material.dart';
import '../utils/location_utils.dart';
 
class SplashPage extends StatelessWidget {
  dynamic a = new LocationUtils();
  Widget build(BuildContext context) {
    return new Material(
      child: Container(
        color: Colors.pink[500],
        child: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              Icon(
                Icons.collections_bookmark,
                size: 150.0,
                color: Colors.white,
              ),
              SizedBox(
                height: 80.0,
              ),
              RaisedButton(
                  child: Text(
                    "Looking for Job",
                    style: new TextStyle(
                      color: Colors.white,
                      fontSize: 28.0,
                    ),
                  ),
                  elevation: 5.0,
                  splashColor: Colors.orangeAccent,
                  color: Colors.orange,
                  padding: EdgeInsets.all(20.0),
                  onPressed: () {
                       Navigator.of(context).pushNamed('/LoginPage');
                  }),
              SizedBox(
                height: 28.0,
              ),
              RaisedButton(
                  child: Text(
                    'Looking for Hire',
                    style: new TextStyle(
                      color: Colors.white,
                      fontSize: 28.0,
                    ),
                  ),
                  elevation: 5.0,
                  splashColor: Colors.greenAccent,
                  padding: EdgeInsets.all(20.0),
                  color: Colors.lightGreen,
                  onPressed: () {
                    Navigator.of(context).pushNamed('/EmployeerHomePage');
                  }),
            ],
          ),
        ),
      ),
    );
  }
}
