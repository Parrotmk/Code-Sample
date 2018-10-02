import 'dart:async';
import 'dart:math';
import 'package:async/async.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:domesticjobs/utils/login_utils.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:flutter/material.dart';
import 'package:flutter_image_pick_crop/flutter_image_pick_crop.dart';
import 'package:google_sign_in/google_sign_in.dart';
import 'package:http_parser/http_parser.dart';
import 'package:intl/intl.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'dart:convert';
import '../../utils/users_utils.dart';
import '../../utils/jobs_utils.dart';
import 'package:flutter/services.dart';
import '../../utils/location_utils.dart';
import 'dart:io';
import 'package:http/http.dart' as http;
import 'package:firebase_database/firebase_database.dart';

class Employeer_HomePage extends StatefulWidget {
  @override
  Employeer_HomePageState createState() => new Employeer_HomePageState();
}

class Employeer_HomePageState extends State<Employeer_HomePage> {
  String _selecteCategory = null;

  String _cName;
  Map _categories;
  JobsUtils _oJobUtils = new JobsUtils();

  GeoPoint lesserGeopoint;
  GeoPoint greaterGeopoint;
  String _platformMessage = 'Unknown';
  String _camera = 'fromCameraCropImage';
  String _gallery = 'fromGalleryCropImage';
  File imageFile;
  Map oAuthUser;
  String imageHost = 'http://192.168.43.223/api/';
  String _photo;

  DocumentReference documentReference;
  @override
  void initState() {
    _photo = imageHost + '/uploads/images/-LL0dcioR2CaD6so8SWd.jpg';
    new UsersUtils().getUserAuthFromSP().then((onValue) {
      setState(() {
        oAuthUser = JSON.decode(onValue);
        documentReference = Firestore.instance
            .collection('users')
            .document(oAuthUser['userDocumentId']);
        _photo = oAuthUser['photo'];
      });
    });

    // GeoPonits for comparing data lat/long
    LocationUtils().getGeoPoint().then((points) {
      setState(() {
        lesserGeopoint = points['lesserGeopoint'];
        greaterGeopoint = points['greaterGeopoint'];
      });
    });

    super.initState();
  }

  void _initGalleryPickUp() {
    UsersUtils().initGalleryPickUp(documentReference).then((responseMessage) {
      documentReference.get().then((snapshots) {
        if (snapshots.exists) {
          snapshots.data['userDocumentId'] = snapshots.documentID;
          UsersUtils().setUserAuthFromSP(JSON.encode(snapshots.data));
          setState(() {
            _photo = snapshots.data['photo'];
          });
        }
      });
    });
  }

  void logoutUser() {
    LoginUtils().logout().then((bool bStatus) {
      if (bStatus == true) {
        Navigator.of(context).pushNamed('/LoginPage');
      }
    });
  }

  void onChanged(val) {
    setState(() {
      _selecteCategory = val;
    });
  }

  Widget build(BuildContext context) {
    try {
      _oJobUtils.getCategories().then((onValue) {
        _categories = onValue;
      });
    } catch (e) {
      print(e.toString());
    }

    return new Material(
      child: new Scaffold(
        drawer: Drawer(
          child: ListView(
            padding: EdgeInsets.zero,
            children: <Widget>[
              DrawerHeader(
                padding: EdgeInsets.all(20.0),
                decoration: BoxDecoration(
                  color: Colors.purpleAccent,
                ),
                child: Row(
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: <Widget>[
                    Container(
                      height: 100.0,
                      width: 100.0,
                      decoration: BoxDecoration(
                        color: Colors.purple,
                        image: new DecorationImage(
                            image: new NetworkImage(_photo),
                            fit: BoxFit.cover,
                            alignment: Alignment.topCenter),
                        borderRadius:
                            new BorderRadius.all(new Radius.circular(101.0)),
                        border: new Border.all(
                          color: Colors.purple,
                          width: 1.5,
                        ),
                      ),
                    ),
                    SizedBox(
                      width: 20.0,
                    ),
                    GestureDetector(
                      child: Row(
                        children: <Widget>[
                          Icon(
                            Icons.edit,
                            color: Colors.white,
                          ),
                          new Text(
                            " Edit Image",
                            style: TextStyle(color: Colors.white),
                          )
                        ],
                      ),
                      onTap: () {
                        showDialog(
                            context: context,
                            child: AlertDialog(
                              content: Container(
                                height: 150.0,
                                child: Center(
                                  child: Column(
                                    mainAxisAlignment: MainAxisAlignment.center,
                                    children: <Widget>[
                                      RaisedButton(
                                          child: Text(
                                            "Pick from Gallery",
                                            style: new TextStyle(
                                              color: Colors.white,
                                              fontSize: 20.0,
                                            ),
                                          ),
                                          elevation: 5.0,
                                          splashColor: Colors.orangeAccent,
                                          color: Colors.orange,
                                          padding: EdgeInsets.all(10.0),
                                          onPressed: () {
                                            _initGalleryPickUp();
                                            // GoogleSignIn().signOut();
                                            // Navigator.of(context).pushNamed('/LoginPage');
                                          }),
                                      SizedBox(
                                        height: 28.0,
                                      ),
                                      RaisedButton(
                                          child: Text(
                                            'Pick from Camera',
                                            style: new TextStyle(
                                              color: Colors.white,
                                              fontSize: 20.0,
                                            ),
                                          ),
                                          elevation: 5.0,
                                          splashColor: Colors.greenAccent,
                                          padding: EdgeInsets.all(10.0),
                                          color: Colors.lightGreen,
                                          onPressed: () {
                                            Navigator
                                                .of(context)
                                                .pushNamed('/HomePage');
                                          }),
                                    ],
                                  ),
                                ),
                              ),
                            ));
                      },
                    ),
                  ],
                ),
              ),
              ListTile(
                  leading: Icon(Icons.home),
                  title: Text('Edit Profile'),
                  onTap: () {
                    Navigator.of(context).pushNamed('/EmployeeEditProfilePage');
                  }),
              ListTile(
                  leading: Icon(Icons.home),
                  title: Text('Manage Jobs'),
                  onTap: () {
                    Navigator.of(context).pushNamed('/EmployeerJobListingPage');
                  }),
              ListTile(
                leading: Icon(Icons.settings),
                title: Text('Setting'),
              ),
              ListTile(
                leading: Icon(Icons.exit_to_app),
                title: Text('Logout'),
                onTap: () {
                  logoutUser();
                },
              ),
            ],
          ),
        ),
        appBar: AppBar(
          title: new Text('Nearby Workers'),

           actions: <Widget>[
            IconButton(
              icon: Icon(
                Icons.category,
                size: 30.0,
              ),
              onPressed: () {
                Navigator.of(context).pushNamed('/CategoryPage');
              },
            ),
          ],
        ),
        body: Container(
            child: new StreamBuilder(
                stream: Firestore.instance
                    .collection('users')
                    .where('coordinates', isGreaterThan: lesserGeopoint)
                    .where('coordinates', isLessThan: greaterGeopoint)
                    .orderBy('coordinates', descending: false)
                    .limit(2)
                    .snapshots(),
                builder: (BuildContext context,
                    AsyncSnapshot<QuerySnapshot> snapshot) {
                  if (snapshot.data == null) {
                    return Container(
                      child: Center(
                        child: CircularProgressIndicator(),
                      ),
                    );
                  } else {
                    return ListView.builder(
                        itemCount: snapshot.data.documents.length,
                        itemBuilder: (BuildContext context, int index) {
                          print("XXXXXXXXXXXXXXXXXXXXXX");
                          print(snapshot.data.documents[index]);
                          return getUsersList(snapshot.data.documents[index]);
                        });
                  }
                })),
        floatingActionButton: FloatingActionButton(
          child: Icon(
            Icons.add,
            color: Colors.white,
          ),
          tooltip: 'Post new Job',
          backgroundColor: Colors.purpleAccent,
          onPressed: () {
            Navigator.of(context).pushNamed('/EmployeeAddjobPage');
          },
        ),
        floatingActionButtonLocation: FloatingActionButtonLocation.centerDocked,
        bottomNavigationBar: BottomAppBar(
          elevation: 20.0,
          color: Colors.purple,
          child: ButtonBar(
            children: <Widget>[
              Icon(
                Icons.add_a_photo,
                color: Colors.white,
              )
            ],
          ),
        ),
      ),
    );
  }

  Widget getUsersList(DocumentSnapshot document) {
    Map data = document.data;
    double width = MediaQuery.of(context).size.width;

    return Card(
      margin: EdgeInsets.all(0.0),
      child: Container(
        child: Padding(
          padding: EdgeInsets.all(0.0),
          child: Row(
            children: <Widget>[
              Container(
                margin: EdgeInsets.only(right: 15.0),
                height: 130.0,
                width: 120.0,
                decoration: BoxDecoration(
                  image: DecorationImage(
                    image: NetworkImage(data['photo']),
                  ),
                ),
              ),
              Container(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: <Widget>[
                    Text(
                      'Manoj Kumar',
                      style: TextStyle(
                          fontWeight: FontWeight.bold, fontSize: 16.0),
                    ),
                    Text('Plumber'),
                    Text('12 years exoerience'),
                    Text('12 years sd sf exoerience'),
                    Row(
                      children: <Widget>[
                        Container(
                          margin: EdgeInsets.all(6.0),
                          width: 40.0,
                          alignment: Alignment.center,
                          decoration: BoxDecoration(
                              color: Colors.redAccent, shape: BoxShape.circle),
                          child: IconButton(
                            alignment: Alignment.center,
                            iconSize: 24.0,
                            onPressed: () {},
                            color: Colors.white,
                            icon: Icon(Icons.location_on),
                          ),
                        ),
                        Container(
                          margin: EdgeInsets.all(6.0),
                          width: 40.0,
                          alignment: Alignment.center,
                          decoration: BoxDecoration(
                              color: Colors.redAccent, shape: BoxShape.circle),
                          child: IconButton(
                            iconSize: 24.0,
                            onPressed: () {},
                            color: Colors.white,
                            icon: Icon(Icons.phone),
                          ),
                        ),
                        Container(
                          margin: EdgeInsets.all(6.0),
                          width: 40.0,
                          alignment: Alignment.center,
                          decoration: BoxDecoration(
                              color: Colors.redAccent, shape: BoxShape.circle),
                          child: IconButton(
                            iconSize: 24.0,
                            onPressed: () {},
                            color: Colors.white,
                            icon: Icon(Icons.mail_outline),
                          ),
                        ),
                      ],
                    ),
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
