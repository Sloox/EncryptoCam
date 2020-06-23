# Safely Cam 
A camera that makes use of androidx.
It has the following features:
- It requires a password at the start of the application, this is not settable.
- Makes use of Kotlin, MVVM, coroutines, Data Binding, Material Design, AndroidX
- Uses camerax
- uses KOIN for dependancy injection
- Tests are included for the viewmodesl & services.
- All pictures are encrypted via AES and are saved in the format of jpc.
- The application automatically decrypts the files when showing it in a recyclerview.
