import 'dart:async';
import 'dart:convert';
import 'package:domesticjobs/utils/location_utils.dart';
import 'package:flutter/material.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:geocoder/geocoder.dart';
import '../utils/login_utils.dart';
import '../utils/users_utils.dart';
import 'package:shared_preferences/shared_preferences.dart';

import 'package:google_sign_in/google_sign_in.dart';

class LoginPage extends StatefulWidget
{
  @override
  LoginPageState createState() => new LoginPageState(); 
}

class LoginPageState extends State<LoginPage>
{
  /**
   * Shared Preference instance
   */
  Future<SharedPreferences> _oAuth = SharedPreferences.getInstance();

  /**
   * Login Utils instance
   */
  final _oLoginUtils = new LoginUtils();

  final _oUsersUtils = new UsersUtils();
  
  double _latitude;
  
  double _longitude;
  
  Address _oLocation;

  /**
   * Initial State of login page
   */
  @override
  void initState() { 
     new UsersUtils().getUserAuthFromSP().then((onValue) {
      if (onValue.isNotEmpty) {
        Navigator.of(context).pushNamed('/SplashPage');
      }
    });
        // Get current Location object
    new LocationUtils().getCurrentLocation().then((Address oLocation) {
      _oLocation = oLocation;
      _latitude = _oLocation.coordinates.latitude;
      _longitude = _oLocation.coordinates.longitude;
    });
    super.initState();
  }
  
  CollectionReference oUsersCollection = Firestore.instance.collection('users');

  /**
   * Login with google 
   */
  void loginWithGoogle1() 
  {
    _oLoginUtils.googleLoginByFirebase().then((oAuthUser) async{
      if(oAuthUser != null)
      {
        print(oAuthUser.email);
          Stream<QuerySnapshot> collectionReference = await Firestore.instance.collection('users').where('email', isEqualTo: oAuthUser.email).snapshots();
          collectionReference.listen((onData){
            if(onData.documents.length <= 0)
            {
              Map aUserData = Map<String, dynamic>();
              aUserData['name'] = oAuthUser.displayName;
              aUserData['email'] = oAuthUser.email;
              aUserData['photo'] = oAuthUser.photoUrl;
              aUserData['providerId'] = oAuthUser.providerId;
              aUserData['uId'] = oAuthUser.uid;
              aUserData['coordinates'] = GeoPoint(_latitude, _longitude);
              aUserData['createdOn'] =  DateTime.now().toUtc();

              Map aUserInfo = _oLoginUtils.saveAuthUser(aUserData);
              _oUsersUtils.setUserAuthFromSP(JSON.encode(aUserInfo));
             }
             else {
                onData.documents[0].data['userDocumentId'] = onData.documents[0].documentID;
 
                _oUsersUtils.setUserAuthFromSP(JSON.encode(onData.documents[0].data));
             } 
          }); 
          Navigator.of(context).pushNamed('/SplashPage');
         
      }
    }); 
 }


  /**
   * GUI for Login Page
   */
  Widget build(BuildContext context)
  {
    return new Material(
      child: new Scaffold(
        body: new Container(

          padding: EdgeInsets.all(45.0),
          decoration: BoxDecoration(
            color: Colors.pink[400],
          ),
          child: new Center( 
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              
              children: <Widget>[
                Icon(Icons.collections_bookmark, size: 150.0, color: Colors.white,),
                Padding(
                  padding: const EdgeInsets.only(top: 50.0),
                  child: new InkWell( 
                    child: new Image.asset('images/google_login.png'),
                    onTap: (){  loginWithGoogle1();
                    CircularProgressIndicator();
                   //  Navigator.of(context).pushNamed('/HomePage');
                    }
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}