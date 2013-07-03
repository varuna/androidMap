androidMap
==========

Sample Map application which displays routing on android maps using the Google Map Web APIs'
In order to see how this work generate your own API key from the Google API console (https://code.google.com/apis/console). Make sure that you give the correct package. In this case its "com.varunarl.mapssample".
Also import the Google Play Services library project to your workspace. Eclipse -> File -> Import -> Existing android code into workspace (Select your SDK -> extras -> google -> google_play_services -> libproject).
Make sure that you import the code by ticking the copy to workspace option. After the import process, Goto the project (MapsSample project) properties. Under the android settings add the library project to dependancy list.

To enable more locations you can define LatLng objects in MainActivity.java file. And feed the GeoPoints to the appropriate classes and methods.


Licence
=======
Maintained by : Varuna Lekamwasam<vrlekamwasam@gmail.com>
Authored By  : Varuna Lekamwasam<vrlekamwasam@gmail.com>

MapsSample is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation.
MapsSample is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU General Public License along with MapsSample. If not, see <http://www.gnu.org/licenses/gpl.html>

