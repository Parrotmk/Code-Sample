import 'dart:convert';

import 'package:domesticjobs/utils/users_utils.dart';
import 'package:flutter/material.dart';
import 'dart:async';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:intl/intl.dart';
import 'package:shared_preferences/shared_preferences.dart';

class Employeer_EditProfilePage extends StatefulWidget {
  @override
  Employeer_EditProfileState createState() => new Employeer_EditProfileState();
}

class Employeer_EditProfileState extends State<Employeer_EditProfilePage> {
  TextEditingController _name = new TextEditingController();
  TextEditingController _email = new TextEditingController();
  DocumentReference documentReference;
  Map oAuthUser;
  final GlobalKey<FormState> _formKey = new GlobalKey<FormState>();
  final GlobalKey<ScaffoldState> _scaffold = new GlobalKey<ScaffoldState>();
  List<DropdownMenuItem> categories;
  @override
  void initState() {
    new UsersUtils().getUserAuthFromSP().then((onValue) async {
      oAuthUser = JSON.decode(onValue);
      _name.text = oAuthUser['name'];
      _email.text = oAuthUser['email'];
    });

    super.initState();
  }

  void _save() {
    if (_formKey.currentState.validate()) {
      _formKey.currentState.save();
      Map data = Map<String, dynamic>();
      data['name'] = _name.text;
      documentReference = Firestore.instance
          .collection('users')
          .document(oAuthUser['userDocumentId']);
      documentReference.updateData(data).whenComplete(() {
        documentReference.get().then((snapshot) {
          snapshot.data['userDocumentId'] = snapshot.documentID;
          UsersUtils().setUserAuthFromSP(JSON.encode(snapshot.data));
        });

        _scaffold.currentState.showSnackBar(new SnackBar(
            content: Row(
              children: <Widget>[
                Icon(Icons.check),
                new Text(" Profile Updated Successfully.")
              ],
            ),
            duration: Duration(seconds: 3)));
      }).catchError((e) {
        _scaffold.currentState.showSnackBar(new SnackBar(content: new Text(e)));
      });
    }
  }

  Widget build(BuildContext context) {
    return new Material(
      child: new Scaffold(
        key: _scaffold,
        appBar: AppBar(
          backgroundColor: Colors.lightBlue,
          title: new Text('Edit Profile'),
        ),
        body: SingleChildScrollView(
          child: Container(
            padding: EdgeInsets.all(20.0),
            child: Form(
              key: this._formKey,
              child: Column(
                children: <Widget>[
                  TextFormField(
                    initialValue: null,
                    controller: _name,
                    keyboardType: TextInputType.text,
                    validator: (name) {
                      if (name.isEmpty) return 'Please enter your name';
                    },
                    decoration: InputDecoration(
                      // hintText:
                      //   snapshot.data.documents.first.data['name'],
                      labelText: 'Name',
                    ),
                    onSaved: (name) {
                      _name.text = name;
                    },
                  ),
                  TextFormField(
                    enabled: false,
                    controller: _email,
                    decoration: InputDecoration(
                      // hintText:
                      //   snapshot.data.documents.first.data['name'],
                      labelText: 'Email Id',
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
                        _save();
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
              ),
            ),
          ),
        ),
      ),
    );
  }
}
