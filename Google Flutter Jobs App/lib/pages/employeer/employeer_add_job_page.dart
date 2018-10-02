import 'dart:async';
import 'dart:convert';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../../utils/users_utils.dart';
import 'package:flutter_google_places_autocomplete/flutter_google_places_autocomplete.dart';
import '../../utils/location_utils.dart';
import 'package:geocoder/geocoder.dart';

class Employeer_AddjobPage extends StatefulWidget {
  @override
  Employeer_AddjobPageState createState() => new Employeer_AddjobPageState();
}

class Employeer_AddjobPageState extends State<Employeer_AddjobPage> {
 
  // Store Slelected category
  String _jobCategory = null;

  // Generate form key
  final _formKey = GlobalKey<FormState>();
  
  String _categoryError;
  
  String _jobTitle;
  
  String _jobDescription;
 
  List<String> _items = new List<String>();
  
  String _userId;
  
  static const kGoogleApiKey = "AIzaS1yBGYOqmttcfHaJwVEV2wISDEAH2HI-qxhG8";
  
  // to get places detail (lat/lng)
  GoogleMapsPlaces _places = new GoogleMapsPlaces(kGoogleApiKey);

  /**
   * Users Utils instance
   */
  final _oUsersUtils = new UsersUtils();
  
  String _locationAddress;
  
  double _latitude;
  
  double _longitude;
  
  Address _oLocation;
  Map _oAuthUser;
  
  /**
   * Save new job
   */

  void _saveJob() {
    if (_jobCategory == null) {
      _categoryError = 'Error category';
      return;
    }

    if (_formKey.currentState.validate()) {
      _formKey.currentState.save();

      //_userId = oAuthUser['name'];
      Map<String, dynamic> data = {
        'job_id': new DateTime.now().millisecondsSinceEpoch,
        'userDocumentId': _oAuthUser['userDocumentId'],
        'title': _jobTitle,
        'description': _jobDescription,
        'categoryId': _jobCategory,
        'createdOn': DateTime.now().toUtc(),
        'jobLocation': _locationAddress,
        'jobCountryName': _oLocation.countryName,
        'jobCountryCode': _oLocation.countryCode,
        'coordinates': GeoPoint(_latitude, _longitude),
        'latitude': _latitude,
        'longitude': _longitude,
        'postalCode': _oLocation.postalCode,
      };

      Firestore.instance.runTransaction((Transaction tns) async {
        CollectionReference collectionReference =
            Firestore.instance.collection('jobs');
        await collectionReference.add(data).then((onValue) {
          Navigator.of(context).pop();
        });
      }).catchError((onError) {});
    }
  }

  CollectionReference _categoriesList;

  List<Placemark> _placemark;

  var jobLocationField = new TextEditingController();
  @override
  void initState() {
    _categoriesList = Firestore.instance.collection('categories');
    _oUsersUtils.getUserAuthFromSP().then((onValue) {
      _oAuthUser = JSON.decode(onValue);
    });

    // Get current Location object
    new LocationUtils().getCurrentLocation().then((Address oLocation) {
      _oLocation = oLocation;
      _latitude = _oLocation.coordinates.latitude;
      _longitude = _oLocation.coordinates.longitude;
      jobLocationField.text = _locationAddress = oLocation.addressLine;
    });

    super.initState();
  }

  void _onChanged(value) {
    setState(() {
      _jobCategory = value;
    });
  }

  Widget build(BuildContext context) {
    return new Material(
      child: new Scaffold(
        appBar: AppBar(
          title: new Text('Add New Job'),
        ),
        body: SingleChildScrollView(
          child: Container(
            padding: EdgeInsets.all(20.0),
            child: Form(
                key: _formKey,
                child: Column(
                  children: <Widget>[
                    Padding(
                      padding: EdgeInsets.only(top: 20.0, bottom: 20.0),
                      child: TextFormField(
                        decoration: InputDecoration(
                          hintText: 'Enter job title',
                          labelText: 'Job title',
                        ),
                        validator: (value) {
                          if (value.isEmpty) {
                            return 'Please enter the job title';
                          }
                        },
                        onSaved: (value) {
                          _jobTitle = value;
                        },
                      ),
                    ),
                    TextFormField(
                      keyboardType: TextInputType.multiline,
                      maxLines: 2,
                      maxLength: 500,
                      decoration: InputDecoration(
                        labelText: 'Job description',
                        errorMaxLines: 5,
                      ),
                      validator: (value) {
                        if (value.isEmpty) {
                          return 'Please enter job description';
                        }
                      },
                      onSaved: (value) {
                        _jobDescription = value;
                      },
                    ),
                    Container(
                      padding: EdgeInsets.only(top: 16.0),
                      width: 300.0,
                      child: StreamBuilder(
                          stream: _categoriesList.snapshots(),
                          builder: (BuildContext context,
                              AsyncSnapshot<QuerySnapshot> snapshot) {
                            if (!snapshot.hasData) {
                              return Container(
                                child: Center(
                                    //child: CircularProgressIndicator(),
                                    ),
                              );
                            } else {
                              if (_jobCategory == null) {
                                _jobCategory =
                                    snapshot.data.documents.first.documentID;
                              }
                              return Align(
                                alignment: Alignment(-1.0, 0.0),
                                child: DropdownButton(
                                  items: snapshot.data.documents.map((item) {
                                    return DropdownMenuItem(
                                      value: item.documentID,
                                      child: new Text(item['name']),
                                    );
                                  }).toList(),
                                  value: _jobCategory,
                                  onChanged: (dynamic val) {
                                    _onChanged(val);
                                  },
                                ),
                              );
                            }
                          }),
                    ),
                    TextFormField(
                      enabled: false,
                      keyboardType: TextInputType.multiline,
                      initialValue: null,
                      maxLines: 2,
                      controller: jobLocationField,
                      decoration: InputDecoration(
                        labelText: 'Job Location',
                        errorMaxLines: 5,
                      ),
                      validator: (value) {
                        if (value.isEmpty) {
                          return 'Location not found';
                        }
                      },
                    ),
                    SizedBox(
                      height: 5.0,
                    ),
                    GestureDetector(
                      onTap: () async {
                        // show input autocomplete with selected mode
                        // then get the Prediction selected
                        Prediction p = await showGooglePlacesAutocomplete(
                            context: context,
                            apiKey: kGoogleApiKey,
                            onError: (res) {},
                            mode: Mode.overlay,
                            language: "en",
                            components: [
                              new Component(Component.country, "IN")
                            ]);

                        displayPrediction(p);
                      },
                      child: Align(
                        alignment: Alignment(1.0, 0.0),
                        child: Text("Click to change location",
                            style: TextStyle(color: Colors.green)),
                      ),
                    ),
                    Padding(
                      padding: EdgeInsets.only(top: 30.0),
                      child: RaisedButton(
                        elevation: 2.0,
                        color: Colors.redAccent,
                        padding: EdgeInsets.only(
                            top: 15.0, bottom: 15.0, left: 40.0, right: 40.0),
                        splashColor: Colors.red,
                        onPressed: () {
                          CircularProgressIndicator();
                          _saveJob();
                        },
                        child: Text(
                          'Save',
                          style: new TextStyle(
                            color: Colors.white,
                            fontSize: 20.0,
                          ),
                        ),
                      ),
                    )
                  ],
                )),
          ),
        ),
      ),
    );
  }

  Future<Null> displayPrediction(Prediction p) async {
    if (p != null) {
      // get detail (lat/lng)
      PlacesDetailsResponse detail =
          await _places.getDetailsByPlaceId(p.placeId, language: 'en');

      new LocationUtils()
          .getAddressByPosition(detail.result.geometry.location.lat,
              detail.result.geometry.location.lng)
          .then((oLocation) {
        _oLocation = oLocation;
      });
      setState(() {
        jobLocationField.text = _locationAddress = p.description;
        _latitude = detail.result.geometry.location.lat;
        _longitude = detail.result.geometry.location.lng;
      });

      /* scaffold.showSnackBar(
        new SnackBar(content: new Text("${p.description} - $lat/$lng")));
        */
    }
  }
}
