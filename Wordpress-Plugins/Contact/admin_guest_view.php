<?php 
global $wpdb;
$id = $_GET['id'];
//echo $id;

if(isset($_POST['submit']))
{
	
	 global $wpdb;
	 $ids= $_GET['id'];
     $status = $_POST['select'];
	 $comments = $_POST['comments'];
	 $name = $_POST['name'];
	 $email = $_POST['email'];
	 $wpdb->query( "UPDATE wp_guestbook  SET  guestbook_approve ='$status',guestbook_comments ='$comments',guestbook_name ='$name',guestbook_email ='$email' WHERE id='$ids'" );
	 ?>
	 <script type="text/javascript">

	   window.location="<?php echo get_admin_url(); ?>/admin.php?page=Guest";

	</script>
	<?php
 } 
$detail = $wpdb->get_results("SELECT * FROM wp_guestbook WHERE id='$id' ");?>

<h1>Guest Details:</h1>
<form action="" method="post" id="guestdetail">
<table width="50%" border="1" class="wp-list-table widefat fixed striped posts">
 <tr>
  <th width="20%">Guest Name:&nbsp;</th>
  <td><input type="text" name="name" value="<?php echo $detail[0]->guestbook_name;?>"></td>
 </tr>
 <tr>
  <th>Guest Email:&nbsp;</th>
  <td><input type="email" name="email" value="<?php echo $detail[0]->guestbook_email;?>"></td>
 </tr>
 <tr>
  <th>Guest Comments:&nbsp;</th>
  <td><textarea name="comments" rows="4" cols="50"><?php echo stripslashes($detail[0]->guestbook_comments);?></textarea></td>
 </tr>
 <tr>
  <th>Created At:&nbsp;</th>
   <?php $date = strtotime($detail[0]->guestbook_created);?>
  <td><?php echo date("j F Y", $date);?></td>
 </tr>
 <tr>
   <th>Action</th>
         <td>
		  <select  class="select" name="select" >  		  
            <option  value="0" <?php if($detail[0]->guestbook_approve == 0) echo 'selected';?>>Pending</option>
            <option  value="1" <?php if($detail[0]->guestbook_approve == 1) echo 'selected';?>>Approve</option>
          </select>
		</td>
 
 </tr>
</table> 
<input type="submit" name="submit" value="update" style="color:white;background-color:green;"> 
</form>

