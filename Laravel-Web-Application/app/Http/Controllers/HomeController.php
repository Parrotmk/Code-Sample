<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Redirect;
use App\Http\Models\User;

class HomeController extends Controller
{
    /***
     * Create a new controller instance.
     *
     * @return void
     */
    public function __construct()
    {
        $this->middleware('auth');
    }

    /**
     * Show the application dashboard.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
		/* Throught Model */
		$users = User::getUsers(); 
        return view('home',['users'=>$users]);
    }
	
	/**
     * delete the user.
     *
     * @return \Illuminate\Http\Response
     */
    public function destroy($id)
    {
		/* 
		  Direct query using query builder 
		*/
		DB::table('users')->where('id', '=', $id)->delete(); 
          return Redirect::to('home');
		  
		      // redirect
        //    Session::flash('message', 'Successfully created nerd!');
         //   return Redirect::to('nerds');
			
			
    }
}
