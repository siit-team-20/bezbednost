# Define parameters
$NewUsername = "booking_baboon_user"
$NewUserPassword = "m0nk3"
$ApplicationDirectoryRelative = ".\..\..\.."  # Relative to the script

# Get the full path to the application directory
$ScriptRoot = $PSScriptRoot
$ApplicationDirectory = Join-Path -Path $ScriptRoot -ChildPath $ApplicationDirectoryRelative

# Step 1: Create a new user account
New-LocalUser -Name $NewUsername -Password (ConvertTo-SecureString -AsPlainText $NewUserPassword -Force) -FullName "Your New User" -Description "Dedicated user for Spring Boot application"

# Step 2: Assign file system permissions
$acl = Get-Acl -Path $ApplicationDirectory
$rule = New-Object System.Security.AccessControl.FileSystemAccessRule($NewUsername, 'FullControl', 'ContainerInherit, ObjectInherit', 'None', 'Allow')
$acl.SetAccessRule($rule)
Set-Acl -Path $ApplicationDirectory -AclObject $acl