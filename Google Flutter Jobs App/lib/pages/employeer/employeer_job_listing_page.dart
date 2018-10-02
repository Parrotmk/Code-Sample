import 'dart:async';
import 'dart:math';
import 'package:async/async.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:domesticjobs/utils/login_utils.dart';
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

class Employeer_JobListingPage extends StatefulWidget {
  @override
  Employeer_JobListingPageState createState() =>
      new Employeer_JobListingPageState();
}

class Employeer_JobListingPageState extends State<Employeer_JobListingPage> {
/**
 * Post a new job
 */

  Future<SharedPreferences> auth = SharedPreferences.getInstance();

  String _selecteCategory = null;

  String _cName;

  Map _categories;

  JobsUtils _oJobUtils = new JobsUtils();

  GeoPoint lesserGeopoint;

  GeoPoint greaterGeopoint;

  File imageFile;

  Map oAuthUser;

  String imageHost = 'http://192.168.43.223/api/';

  String _photo;

  DocumentReference documentReference;

  final GlobalKey<ScaffoldState> _scaffold = new GlobalKey<ScaffoldState>();

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

      if (responseMessage != null) {
        if (responseMessage['code'] == '1') {
          _scaffold.currentState.showSnackBar(SnackBar(
              content: Row(
                children: <Widget>[
                  Icon(Icons.check),
                  Text(responseMessage['message'])
                ],
              ),
              duration: Duration(seconds: 3)));
        } else if (responseMessage['code'] == '0') {
          _scaffold.currentState.showSnackBar(
              SnackBar(content: Text(responseMessage['message'])));
        } else {
          _scaffold.currentState.showSnackBar(
              SnackBar(content: Text(responseMessage['message'])));
        }
      }
    });
  }

  void _deleteJob(DocumentSnapshot document) {
    Firestore.instance.runTransaction((Transaction transactionHandler) async {
      await transactionHandler.get(document.reference);
      await transactionHandler.delete(document.reference);
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
        key: _scaffold,
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
                    Navigator
                        .of(context)
                        .pushNamed('/EmployeerEditProfilePage');
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
          title: new Text('Manage Jobs'),
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
                    .collection('jobs')
                    // .where('coordinates', isGreaterThan: lesserGeopoint)
                    // .where('coordinates', isLessThan: greaterGeopoint)
                    .where('userDocumentId',
                        isEqualTo: oAuthUser['userDocumentId'])
                    .orderBy('job_id', descending: true)
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
                          return getJobList(snapshot.data.documents[index]);
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
            Navigator.of(context).pushNamed('/EmployeerAddjobPage');
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

  Widget getJobList(DocumentSnapshot document) {
  
    Map data = document.data;

    String _workerName = null;
 
    return Row(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: <Widget>[
        Expanded(
            child: Container(
          padding:
              EdgeInsets.only(top: 15.0, left: 15.0, right: 15.0, bottom: 5.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.start,
            children: <Widget>[
              Align(
                alignment: Alignment(-1.0, 0.0),
                child: Text(
                  data['title'],
                  style: TextStyle(
                      fontSize: 17.0,
                      fontWeight: FontWeight.w500,
                      letterSpacing: 0.5,
                      color: Colors.pinkAccent),
                ),
              ),
              SizedBox(
                height: 5.0,
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.start,
                children: <Widget>[
                  Icon(
                    Icons.timer,
                    size: 18.0,
                    color: Colors.purple[300],
                  ),
                  Padding(
                    padding: const EdgeInsets.only(left: 8.0),
                    child: Text(DateFormat("MMMM dd, yyyy   hh:mm a")
                        .format(
                            DateTime.fromMillisecondsSinceEpoch(data['job_id']))
                        .toString()),
                  ),
                  Align(
                      alignment: Alignment(2.0, 0.0),
                      widthFactor: 2.0,
                      child: IconButton(
                        icon: Icon(Icons.delete, size: 20.0, color: Colors.red),
                        onPressed: () {
                          AlertDialog alertDialog = AlertDialog(
                            contentPadding: EdgeInsets.all(10.0),
                            content: Container(
                              height: 125.0,
                              child: Column(
                                children: <Widget>[
                                  Padding(
                                    padding: EdgeInsets.only(top: 20.0),
                                    child:
                                        Text("Do you want to delete this job?"),
                                  ),
                                  Padding(
                                    padding: EdgeInsets.only(top: 20.0),
                                    child: Row(
                                      mainAxisAlignment:
                                          MainAxisAlignment.spaceAround,
                                      children: <Widget>[
                                        IconButton(
                                          icon: Icon(Icons.check, size: 30.0),
                                          onPressed: () {
                                            _deleteJob(document);
                                            Navigator.of(context).pop();
                                            CircularProgressIndicator();
                                          },
                                        ),
                                        IconButton(
                                          icon: Icon(Icons.close, size: 30.0),
                                          onPressed: () {
                                            Navigator.of(context).pop();
                                          },
                                        )
                                      ],
                                    ),
                                  )
                                ],
                              ),
                            ),
                          );
                          showDialog(context: context, child: alertDialog);
                        },
                      )),
                ],
              ),
              Expanded(
                flex: 0,
                child: Align(
                  alignment: Alignment(-1.0, 0.0),
                  child: Text(
                    data['description'].toString().length > 150
                        ? data['description'].toString().substring(0, 150) +
                            '....'
                        : data['description'],
                    style: TextStyle(letterSpacing: 0.5),
                  ),
                ),
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.start,
                children: <Widget>[
                  Icon(
                    Icons.category,
                    size: 18.0,
                    color: Colors.purple[300],
                  ),
                  Padding(
                    padding: EdgeInsets.all(15.0),
                    child: Text(_categories[data['categoryId']] ?? ''),
                  ),
                ],
              ),
              Container(
                margin: EdgeInsets.only(top: 20.0),
                decoration: BoxDecoration(
                    border: Border(
                  bottom: BorderSide(color: Colors.grey[200]),
                  top: BorderSide(color: Colors.grey[200]),
                )),
                child: Row(
                  children: <Widget>[
                    FlatButton(
                        onPressed: () {},
                        child: Row(
                          children: <Widget>[
                            Icon(
                              Icons.work,
                              color: Colors.orange,
                            ),
                            Padding(
                              padding: const EdgeInsets.all(8.0),
                              child: Text(
                                "hh",
                              ),
                            ),
                          ],
                        )),
                    FlatButton(
                        onPressed: () {},
                        child: Row(
                          children: <Widget>[
                            Icon(
                              Icons.work,
                              color: Colors.orange,
                            ),
                            Padding(
                              padding: const EdgeInsets.all(8.0),
                              child: Text(
                                _workerName == null ? 'no worker' : _workerName,
                              ),
                            ),
                          ],
                        )),
                  ],
                ),
              ),
            ],
          ),
        )),
      ],
    );
  }
}
