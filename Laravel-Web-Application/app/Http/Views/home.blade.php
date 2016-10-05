@extends('layouts.app')

@section('content')
<div class="container">
    <div class="row">
        <div class="col-md-8 col-md-offset-2">
            <div class="panel panel-default">
                <div class="panel-heading">User Dashboard</div>

                <div class="panel-body">
                  
				 <table class="table table-striped">
    <thead>
      <tr>
        <th>Name</th>
        <th>Email</th>
        <th>Created At</th>
		<th>Action</th>
      </tr>
    </thead>
    <tbody>
	@foreach ($users as $user)
   <tr>
        <td>{!! $user->name !!}</td>
        <td>{!! $user->email !!}</td>
        <td>{!! $user->created_at !!}</td>
		<td><a href="{{URL::to('/home/'. $user->id)}}">Delete</a></td>
      </tr>
    @endforeach

      
    </tbody>
  </table>
					
					
                </div>
            </div>
        </div>
    </div>
</div>
@endsection
