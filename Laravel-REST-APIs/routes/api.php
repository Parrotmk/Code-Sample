<?php

use Illuminate\Http\Request;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/


Route::group(array('prefix' => 'api'), function() {
	/* Get All users */
    Route::get('/user','APIController@show');
	
	/* Get a user by Id */
	Route::get('/user/{id}','APIController@get');
});