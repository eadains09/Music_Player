import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class SongLibrary {
	ArrayList<Song> songLibrary;

	SongLibrary()
	{
		songLibrary = new ArrayList<Song>();
	}
	
	public void addSong(Song currSong)
	{
		songLibrary.add(currSong);
	}
	
	public Song getSong(int i)
	{
		return songLibrary.get(i);
	}
	
	public int librarySize()
	{
		return songLibrary.size();
	}
	
	public void saveLibrary(File f)
	{
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			bw.write(Integer.toString(songLibrary.size()) + "\n");
			for (int i=0; i<songLibrary.size(); i++)
			{
				bw.write(songLibrary.get(i).songTitle + "\n");
				bw.write(songLibrary.get(i).artist + "\n");
				bw.write(songLibrary.get(i).albumName + "\n");
				bw.write(songLibrary.get(i).genre + "\n");
				bw.write(songLibrary.get(i).songPath + "\n");
				bw.write("\n");
			}
			
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void loadLibrary(File f)
	{
		songLibrary = new ArrayList<Song>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			int size = Integer.parseInt(br.readLine());
			for (int i=0; i<size; i++)
			{
				String title = br.readLine();
				String artist = br.readLine();
				String album = br.readLine();
				String genre = br.readLine();
				String path = br.readLine();
				br.readLine();
				
				Song currSong = new Song(title, artist, album, genre, path);
				songLibrary.add(currSong);
			}
			br.close();
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sortSongLibrary()
	{
		for (int i=0; i<songLibrary.size()-1; i++)
		{
			for (int k=i+1; k<songLibrary.size(); k++)
			{
				if (songLibrary.get(i).compareTo(songLibrary.get(k))>0)
				{
					swapSongs(i, k);
				}
			}
		}
	}
	
	public void swapSongs(int a, int b)
	{
		Song temp = songLibrary.get(a);
		songLibrary.set(a, songLibrary.get(b));
		songLibrary.set(b, temp);
	}
}
