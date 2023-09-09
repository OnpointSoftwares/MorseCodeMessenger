package com.example.morsecodemessenger

import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import com.example.morsecodemessenger.models.user
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream
import java.util.UUID

class Registration : AppCompatActivity() {
    private lateinit var imageUrl: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        val selectImage:Button=findViewById(R.id.selectImage)
        val username:EditText=findViewById(R.id.username)
        val email:EditText=findViewById(R.id.user_email)
        val password:EditText=findViewById(R.id.user_password)
        val btnLogin:MaterialButton=findViewById(R.id.login_btn)
        val btnSignup:MaterialButton=findViewById(R.id.signup_btn)
        val phone:EditText=findViewById(R.id.phone)
        btnLogin.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }
        btnSignup.setOnClickListener {
            if(username.text.toString()!=""||email.text.toString()!=""||password.text.toString()!=""||phone.text.toString()!="") {
                handleSignup(
                    username.text.toString(),
                    email.text.toString(),
                    password.text.toString(),
                    phone.text.toString()
                )
            }
            else{
                val alertDialog=AlertDialog.Builder(this)
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert)
                alertDialog.setTitle("Empty Fields")
                alertDialog.setMessage("Please make sure filled all the details")
                alertDialog.setPositiveButton("Done") {dialog, _ ->
                    dialog.dismiss()
                }
                alertDialog.create().show()
                }
            }
        selectImage.setOnClickListener {
            selectImage()
        }
    }

    private fun handleSignup(username: String, email: String, password: String, phone: String) {
        val ref=FirebaseDatabase.getInstance().reference
        val auth= FirebaseAuth.getInstance()

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            if(it.isSuccessful)
            {
                ref.child("Users").child(it.result.user!!.uid).setValue(user(phone,username,email,password,imageUrl.toString())).addOnCompleteListener {task->
                    if(task.isSuccessful)
                    {
                        Toast.makeText(this,"Registration Successful",Toast.LENGTH_LONG).show()
                    }
                }
            }
            else{
                Toast.makeText(this,"Error logging in",Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode === 123) {
            val photo=data!!.extras!!["data"] as Bitmap
            // BitMap is data structure of image file which store the image in memory
            // Set the image in imageview for display
            uploadImage(photo)
            findViewById<CircleImageView>(R.id.profile_pic)?.setImageBitmap(photo)
        }
        else if(requestCode===100) {
            val imageSelected = data?.data
            imageUrl=imageSelected!!.path!!.toUri()
            val pathColumn = arrayOf(MediaStore.Images.Media.DATA)
            if (imageSelected != null) {
                val contentResolver=this.contentResolver
                val myCursor = contentResolver.query(
                    imageSelected,
                    pathColumn, null, null, null
                )
                // Setting the image to the ImageView
                if (myCursor != null) {
                    myCursor.moveToFirst()
                    val columnIndex = myCursor.getColumnIndex(pathColumn[0])
                    val picturePath = myCursor.getString(columnIndex)
                    val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageSelected)
                    findViewById<CircleImageView>(R.id.profile_pic)
                        ?.setImageBitmap(bitmap)
                    uploadImage(imageSelected)
                    myCursor.close()
                }
            }
        }
    }
    fun uploadImage(fileUri: Uri) {
        // on below line checking weather our file uri is null or not.
        if (fileUri != null) {
            // on below line displaying a progress dialog when uploading an image.
            val progressDialog = ProgressDialog(this)
            // on below line setting title and message for our progress dialog and displaying our progress dialog.
            progressDialog.setTitle("Uploading...")
            progressDialog.setMessage("Uploading your image..")
            progressDialog.show()

            // on below line creating a storage refrence for firebase storage and creating a child in it with
            // random uuid.
            val ref: StorageReference = FirebaseStorage.getInstance().getReference()
                .child(UUID.randomUUID().toString())
            // on below line adding a file to our storage.
            ref.putFile(fileUri!!).addOnSuccessListener {
                // this method is called when file is uploaded.
                // in this case we are dismissing our progress dialog and displaying a toast message
                it.metadata!!.reference!!.downloadUrl.addOnSuccessListener {uri->
                    imageUrl=uri
                }
                progressDialog.dismiss()
                Toast.makeText(this, "Image Uploaded..", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                // this method is called when there is failure in file upload.
                // in this case we are dismissing the dialog and displaying toast message
                progressDialog.dismiss()
                Toast.makeText(this, "Fail to Upload Image..", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
    fun uploadImage(fileUri: Bitmap) {
        // on below line checking weather our file uri is null or not.
        if (fileUri != null) {
            // on below line displaying a progress dialog when uploading an image.
            val progressDialog = ProgressDialog(this)
            // on below line setting title and message for our progress dialog and displaying our progress dialog.
            progressDialog.setTitle("Uploading...")
            progressDialog.setMessage("Uploading your image..")
            progressDialog.show()

            // on below line creating a storage refrence for firebase storage and creating a child in it with
            // random uuid.
            val ref: StorageReference = FirebaseStorage.getInstance().getReference()
                .child(UUID.randomUUID().toString())
            // on below line adding a file to our storage.
            val baos= ByteArrayOutputStream()
            fileUri.compress(Bitmap.CompressFormat.JPEG,100,baos)
            val data=baos.toByteArray()
            ref.putBytes(data).addOnSuccessListener {
                // this method is called when file is uploaded.
                // in this case we are dismissing our progress dialog and displaying a toast message
                it.metadata!!.reference!!.downloadUrl.addOnSuccessListener {uri->
                    imageUrl=uri
                }
                progressDialog.dismiss()
                Toast.makeText(this, "Image Uploaded..", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                // this method is called when there is failure in file upload.
                // in this case we are dismissing the dialog and displaying toast message
                progressDialog.dismiss()
                Toast.makeText(this, "Fail to Upload Image..", Toast.LENGTH_SHORT)
                    .show()
            }.addOnCompleteListener {
                if(it.isComplete)
                {
                    Toast.makeText(this,imageUrl.toString(),Toast.LENGTH_LONG).show()
                }
            }
        }
    }



    private fun openCamera() {
            val camera_intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(camera_intent, 123)
        }
        private fun selectImage() {
            val choice = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
            val myAlertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
            myAlertDialog.setTitle("Select Image")
            myAlertDialog.setItems(choice, DialogInterface.OnClickListener { dialog, item ->
                when {
                    // Select "Choose from Gallery" to pick image from gallery
                    choice[item] == "Choose from Gallery" -> {
                        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                        startActivityForResult(gallery, 100)
                    }
                    // Select "Take Photo" to take a photo
                    choice[item] == "Take Photo" -> {
                        openCamera()
                    }
                    // Select "Cancel" to cancel the task
                    choice[item] == "Cancel" -> {
                        dialog.dismiss()
                    }
                }
            })
            myAlertDialog.show()
        }

}