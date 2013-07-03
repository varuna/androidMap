package com.varunarl.mapssample;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends Activity {

	private static final LatLng SLIIT = new LatLng(6.915238, 79.973243);
	private static final LatLng VIRTUSA = new LatLng(6.940695, 79.879701);
	private GoogleMap mGoogleMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mGoogleMap = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.map)).getMap();
		mGoogleMap
				.addMarker(new MarkerOptions().position(SLIIT).title("SLIIT"));
		mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(SLIIT));
		mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(13), 2000, null);
		Route r = new Route(mGoogleMap);
		r.execute(new GoogleMapInputs(SLIIT, VIRTUSA));
	}

	private class GoogleMapInputs {
		LatLng source;
		LatLng destination;

		public GoogleMapInputs(LatLng source, LatLng destination) {
			this.source = source;
			this.destination = destination;
		}

	}

	private class Route extends AsyncTask<GoogleMapInputs, Void, List<LatLng>> {

		private GoogleMap mMap;

		public Route(GoogleMap map) {
			super();
			mMap = map;
		}

		@Override
		protected List<LatLng> doInBackground(GoogleMapInputs... params) {
			LatLng s = params[0].source;
			LatLng d = params[0].destination;
			//Using the GoogleMaps API's get driving directions
			HttpGet req = new HttpGet(
					"http://maps.googleapis.com/maps/api/directions/json?origin="
							+ s.latitude + "," + s.longitude + "&destination="
							+ d.latitude + "," + d.longitude + "&sensor=false");
			
			DefaultHttpClient client = new DefaultHttpClient();
			try {
				HttpResponse res = client.execute(req);
				InputStream out = res.getEntity().getContent();
				return read(out);

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(List<LatLng> result) {
			drawPath(result);
			super.onPostExecute(result);
		}

		private List<LatLng> read(InputStream in) {
			List<LatLng> out = new ArrayList<LatLng>();
			try {
				JsonReader reader = new JsonReader(new InputStreamReader(in,
						"UTF-8"));
				reader.beginObject();
				reader.nextName();
				reader.beginArray();
				reader.beginObject();
				reader.nextName();
				reader.skipValue();
				reader.nextName();
				reader.skipValue();

				reader.nextName();

				reader.beginArray();
				reader.beginObject();
				reader.nextName();
				reader.skipValue();
				reader.nextName();
				reader.skipValue();
				reader.nextName();
				reader.skipValue();
				reader.nextName();
				reader.skipValue();
				reader.nextName();
				reader.skipValue();

				reader.nextName();
				reader.skipValue();

				out = getSteps(reader);

				reader.nextName();
				reader.skipValue();
				reader.endObject();
				reader.endArray();

				// Skipping overview_polyline
				reader.nextName();
				reader.skipValue();
				// skipping summary
				reader.nextName();
				reader.skipValue();
				// skipping warnings
				reader.nextName();
				reader.skipValue();
				// skipping waypoint_order
				reader.nextName();
				reader.skipValue();

				reader.endObject();
				reader.endArray();

				// skipping status
				reader.nextName();
				reader.skipValue();

				reader.endObject();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return out;
		}

		private List<LatLng> getSteps(JsonReader reader) throws IOException {
			List<LatLng> out = new ArrayList<LatLng>();
			reader.nextName();
			reader.beginArray();
			LatLng previousEnd = null;
			while (reader.hasNext()) {
				reader.beginObject();
				LatLng end = null, start = null;
				while (reader.hasNext()) {
					String name = reader.nextName();
					if (name.equalsIgnoreCase("end_location")) {
						reader.beginObject();
						reader.nextName();
						double end_lat = reader.nextDouble();
						reader.nextName();
						double end_lng = reader.nextDouble();
						reader.endObject();
						end = new LatLng(end_lat, end_lng);
					} else if (name.equalsIgnoreCase("start_location")) {
						reader.beginObject();
						reader.nextName();
						double start_lat = reader.nextDouble();
						reader.nextName();
						double start_lng = reader.nextDouble();
						reader.endObject();
						start = new LatLng(start_lat, start_lng);
					} else {
						reader.skipValue();
					}
					if (start != null && end != null) {
						if (previousEnd == null) {
							out.add(start);
							out.add(end);
						} else {
							out.add(end);
						}
						previousEnd = end;
						start = null;
						end = null;
					}
				}
				// Log.i("TEST", reader.nextName());
				reader.endObject();
			}
			reader.endArray();
			return out;
		}

		private void drawPath(List<LatLng> points) {
			mMap.addPolyline(new PolylineOptions().addAll(points).width(2)
					.color(Color.RED));
		}

	}

}
