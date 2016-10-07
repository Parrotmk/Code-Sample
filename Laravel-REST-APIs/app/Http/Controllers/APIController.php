<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Redirect;
use App\Http\Models\User;

class APIController extends Controller
{
    /***
     * Create a new controller instance.
     *
     * @return void
     */
    public function __construct()
    {
     
    }

    /**
     * Show the application dashboard.
     *
     * @return \Illuminate\Http\Response
     */
    public function show()
    {
		/* Throught Model */
		$users = User::getUsers(); 
        return $users;
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
		  
    }
	
	 public function get($id)
    {
		$users = User::getUser($id); 
        return $users;
        
		
			
			
    }
}
