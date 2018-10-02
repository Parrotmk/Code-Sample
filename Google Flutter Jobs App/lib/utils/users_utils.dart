import 'dart:async';
import 'dart:convert';
import 'dart:io';
import 'dart:math';
import 'package:async/async.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter_image_pick_crop/flutter_image_pick_crop.dart';
import 'package:http_parser/http_parser.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:http/http.dart' as http;

class UsersUtils {
  /**
   * Shared preference object variable
   */
  SharedPreferences _oAuth;
  Map _responseMessage = Map<String, dynamic>();
  /**
   * Get Logged-in user auth information
   */
  Future<String> getUserAuthFromSP() async {
    _oAuth = await SharedPreferences.getInstance();
 
    return _oAuth.getString('userAuth') ?? '';
  }

  /**
   * Set Logged-in user auth information
   */
  void setUserAuthFromSP(String userAuth) async {
    _oAuth = await SharedPreferences.getInstance();
    _oAuth.setString('userAuth', userAuth);
  }

  /**
   * Edit profile image
   */

  Future<Map> initGalleryPickUp(documentReference) async {
    File file;
    String result;
    String _camera = 'fromCameraCropImage';
    String _gallery = 'fromGalleryCropImage';
    File imageFile;
    Map oAuthUser;
    String imageHost = 'http://192.168.43.223/api';

    try {
      result = await FlutterImagePickCrop.pickAndCropImage(_gallery);
    } catch (e) {
      result = e.message;
 
    }

    //if (!mounted) return;

    imageFile = new File(result);

    var stream =
        new http.ByteStream(DelegatingStream.typed(imageFile.openRead()));
    var length = await imageFile.length();
    var uri = Uri.parse(imageHost + '/index.php/users/save_profile_image');
    var request = new http.MultipartRequest("POST", uri);
    var rand = new Random();

    Map data = Map<String, dynamic>();
 
    String filename = documentReference.documentID +
        '-' +
        rand.nextInt(1000).toString() +
        '.jpg';
    var multipartFile = new http.MultipartFile('pro_basic_pic', stream, length,
        filename: filename);
    contentType:
    new MediaType('image', '*');

    request.files.add(multipartFile);
    dynamic oResponse = await request.send();

    data['photo'] = imageHost + '/uploads/images/' + filename;
    oResponse.stream.transform(utf8.decoder).listen((response) async {
      Map jsonResponse = JSON.decode(response);
      if (jsonResponse['responseCode'] == '1') {
        await documentReference.updateData(data).whenComplete(() {
          _responseMessage['code'] = 1;
          _responseMessage['message'] = " Profile Updated Successfully.";
        });
      } else {
        _responseMessage['code'] = 1;
        _responseMessage['message'] = " There is some error. please try again.";
      }
      return _responseMessage;
    });
    return _responseMessage;
  }
}
