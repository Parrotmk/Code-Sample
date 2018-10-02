import 'dart:async';
import 'package:flutter/material.dart';
import 'package:domesticjobs/utils/users_utils.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:google_sign_in/google_sign_in.dart';
import 'package:cloud_firestore/cloud_firestore.dart';

import 'package:shared_preferences/shared_preferences.dart';

class LoginUtils {
  /**
  * Google sign in 
  */
  Future<FirebaseUser> googleLoginByFirebase() async {
    GoogleSignInAccount oGoogleSignInAccount = await GoogleSignIn().signIn();
    GoogleSignInAuthentication oGoogleSignInAuthentication =
        await oGoogleSignInAccount.authentication;
    FirebaseUser oUser = await FirebaseAuth.instance.signInWithGoogle(
        idToken: oGoogleSignInAuthentication.idToken,
        accessToken: oGoogleSignInAuthentication.accessToken);
 
    return oUser;
  }

  /**
   * Save user logged-in data to user table
   * @param Map aAuthUser
   */
  Map saveAuthUser(Map aAuthUser) {
    Firestore.instance
        .collection('users')
        .add(aAuthUser)
        .then((DocumentReference oDocument) {
      aAuthUser['userDocumentId'] = oDocument.documentID;
      aAuthUser['model'] = 'users';
    });
    return aAuthUser;
  }

  Future<bool> logout() async{
    bool bStatus = false;
    await new UsersUtils().getUserAuthFromSP().then((onValue) {
      if (onValue.isNotEmpty) {
         new UsersUtils().setUserAuthFromSP('');
        GoogleSignIn().signOut();
        bStatus = true;
      }
    });
    return bStatus;
  }
}
