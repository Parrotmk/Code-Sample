<?php
global $wpdb;
$items_per_page = 30;
$customPagHTML     = "";
$page  = isset( $_GET['cpage'] ) ? abs( (int) $_GET['cpage'] ) : 1;
$offset = ( $page * $items_per_page ) - $items_per_page;
$result = $wpdb->get_results("SELECT * FROM wp_guestbook ORDER BY guestbook_created DESC  LIMIT ${offset}, ${items_per_page}");
$total =  $wpdb->get_results("SELECT * FROM wp_guestbook");
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
if(isset($_POST['submit'])){
	if($_POST['checkbox'] == null){
		$error = "<span style='color:red;'>Please select atlest one</span>";
	}
	else {global $wpdb;
	foreach($_POST['checkbox']  as $data)
	{
	 $wpdb ->query("DELETE FROM wp_guestbook WHERE id=".$data);
	}
	 $success = "<span style='color:green'>Deleted Successfully</span>";?>
	<script type="text/javascript">
	   window.location="<?php echo get_admin_url(); ?>/admin.php?page=Guest";
	</script>
<?php } }
?>

<div id="reponse"></div>
<h1>Guest Book</h1>
<form action="" method="post" id="guest" >
<div class="col-sm-5">
<?php if(isset($error)){echo $error;}?>
<?php if(isset($success)){echo $success;}?>
</div>
<div align="left" class="col-sm-5">
<input type="submit" value="Delete All" name="submit"  Onclick="ConfirmDelete(event);">
</div>
<br>
  <table width="100%" border="1" id="record" class="wp-list-table widefat fixed striped posts">
	  <thead>
           <th width="20"><input type="checkbox" name="selectAll" id="selectAll" ></th>
		   <th style="color:blue;">Guest Name</th>
		   <th style="color:blue;">Guest Email</th>
		   <th style="color:blue;">Guest Comments</th>
		   <th style="color:blue;">Action</th>
	  </thead>
		  <?php  
		  foreach ($result as $data)
		  {
		  ?>
	  <tr>
		  <td  width="20"><input type="checkbox" name="checkbox[]" value="<?php echo $data->id;?>" class="multicheck"></td>
		  <td><?php echo $data->guestbook_name;?></td>
		  <td><?php echo $data->guestbook_email; ?></td>
		  <td style="color:<?php if($data->guestbook_approve == 0) {echo "red";}?>"><?php echo stripslashes(substr($data->guestbook_comments,0,100)).'...<a href="admin.php?page=view&view=view&id='.$data->id.'">Read more</a>';?></td>
		  <td> <a href="admin.php?page=Guest&del=delete&id=<?php echo $data->id;?>" Onclick="ConfirmDelete(event);">Delete</a></td>
        <!---<td><?php echo"<a href=admin.php?page=Guest&del=delete&id=".$data->id." >Delete</a>&nbsp;&nbsp;&nbsp;&nbsp;" ;?></td>-->
	  </tr>
		  <?php  } ?>
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
	 $wpdb ->query("DELETE FROM wp_guestbook WHERE id=".$id);
	?>
	<script type="text/javascript">

	   window.location="<?php echo get_admin_url(); ?>/admin.php?page=Guest";

	</script>
	<?php
	exit;
}
	
	

