import 'package:flutter/material.dart';
import 'package:google_sign_in/google_sign_in.dart';
import 'pages/login_page.dart';
import 'pages/splash_page.dart';
import 'pages/category_page.dart';
import 'pages/employeer/employeer_edit_profile_page.dart';
import 'pages/employeer/employeer_add_job_page.dart';
import 'pages/employeer/employeer_home_page.dart';
import 'pages/employeer/employeer_job_listing_page.dart';

import 'utils/users_utils.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
 dynamic _defaultPage =  SplashPage();
 
 
  @override
  Widget build(BuildContext context) {
    new UsersUtils().getUserAuthFromSP().then((onValue) {
      print("ASASA"); print(onValue);
       if (onValue == null || onValue.isEmpty) {
        _defaultPage = LoginPage();
      }
    });
    return MaterialApp(
      title: 'Flutter - Job App',
      theme: ThemeData(
          primarySwatch: Colors.purple, buttonColor: Colors.limeAccent),
      home: _defaultPage,
      routes: <String, WidgetBuilder>{
        '/EmployeerHomePage': (BuildContext context) => Employeer_HomePage(),
        '/LoginPage': (BuildContext context) => LoginPage(),
        '/SplashPage': (BuildContext context) => SplashPage(),
        '/CategoryPage': (BuildContext context) => CategoryPage(),
        '/EmployeerJobListingPage': (BuildContext context) => Employeer_JobListingPage(),
        '/EmployeerEditProfilePage': (BuildContext context) => Employeer_EditProfilePage(),
        '/EmployeerAddjobPage': (BuildContext context) => Employeer_AddjobPage(),
      },
    );
  }
}
