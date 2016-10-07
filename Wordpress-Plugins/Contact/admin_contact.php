<?php
global $wpdb;
$items_per_page = 30;
$customPagHTML     = "";
$page  = isset( $_GET['cpage'] ) ? abs( (int) $_GET['cpage'] ) : 1;
$offset = ( $page * $items_per_page ) - $items_per_page;
$result = $wpdb->get_results("SELECT * FROM wp_contactus ORDER BY contact_created DESC  LIMIT ${offset}, ${items_per_page}");
$total =  $wpdb->get_results("SELECT * FROM wp_contactus");
$totalPage   = ceil(count($total) / $items_per_page);
if($totalPage > 1){
$customPagHTML     = paginate_links( array(
'base' => add_query_arg( 'cpage', '%#%' ),
'format' => '',
'prev_text' => __('&laquo;'),
'next_text' => __('&raquo;'),
'total' => $totalPage,
'current' => $page
)).'</div>';
}
//echo "<pre>";print_r($result);die;
if(isset($_POST['submit'])){
		if($_POST['checkbox'] == null){
		$error = "<span style='color:red;'>Please select atlest one</span>";
	}
	else{global $wpdb;
	//print_r($_POST['checkbox']);die;
	foreach($_POST['checkbox']  as $data)
	{
	 $wpdb ->query("DELETE FROM wp_contactus WHERE id=".$data);
	}
	$success = "<span style='color:green'>Deleted Successfully</span>";?>
	<script type="text/javascript">

	  window.location="<?php echo get_admin_url(); ?>/admin.php?page=contact";

	</script>
<?php } }
?>


<div id="reponse"></div>
<h1>Contacts</h1>
<form action="" method="post" id="guest">
<div align="left" class="col-sm-5">
<input type="submit" value="Delete All" name="submit"  Onclick="ConfirmDelete(event);">
</div>
<br>
<div class="col-sm-5">
<?php if(isset($error)){echo $error;}?>
<?php if(isset($success)){echo $success;}?>
</div>

  <table width="100%" border="1" class="wp-list-table widefat fixed striped posts">
	  <thead>
           <th width="20"><input type="checkbox" name="selectAll" id="selectAll" ></th>
		   <th>Name</th>
		   <th>Email</th>
		   <th>Phone no</th>
		   <th>Messages</th>
		   <th>Action</th>
	  </thead>
		  
		  <?php foreach ($result as $data)
		  {
		  ?>
	  <tr>
		  <td width="20"><input type="checkbox" name="checkbox[]" value="<?php echo $data->id;?>" class="multicheck"></td>
		  <td><?php echo $data->contact_firstname." ". $data->contact_lastname;?></td>
		  <td><?php echo $data->contact_email; ?></td>
		  <td><?php echo $data->contact_ph; ?></td>
		  <td>
		  <?php $messlength = strlen($data->contact_message);
		  if($messlength > 50){
		  echo stripslashes(substr($data->contact_message,0,50));
		  }
		  else {
			  echo stripslashes($data->contact_message);
		  }
		  ?>
		  </td>
		  <td><a href="admin.php?page=contact&del=delete&id=<?php echo $data->id;?>" Onclick="ConfirmDelete(event);">Delete</a>
		  <a href="admin.php?page=contactview&view=view&id=<?php echo $data->id;?>">View</a>
		  </td>
	  </tr>
		  <?php $row++; } ?>
  </table>
  <?php echo $customPagHTML;?>
 </form>
 
 <script>
    function ConfirmDelete(e)
    {
      var x = confirm("Are you sure you want to delete?");
      if (!x)
         e.preventDefault();
        return false;
    }
	jQuery(document).ready(function(){
	 jQuery('#selectAll').click(function(event){
		 if(this.checked)
		 {
			 jQuery('.multicheck').each(function(){
				 this.checked=true;
			 });
		 }
		 else
		 {
			 jQuery('.multicheck').each(function(){
				 this.checked=false;
			 });
		 }
	 });
 });
</script>
  <?php
  if(isset($_GET['del']))
 {
	 global $wpdb;
	 $id= ($_GET['id']);
	 $wpdb ->query("DELETE FROM wp_contactus WHERE id=".$id);
	 //$adminurl=get_admin_url();
	?>
	<script type="text/javascript">

	   window.location="<?php echo get_admin_url(); ?>/admin.php?page=contact";

	</script>
	<?php
	exit;
}
		  

