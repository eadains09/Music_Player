
public class Song implements Comparable<Song>{
	String artist;
	String albumName;
	String songTitle;
	String genre;
	String songPath;
	
	Song(String title, String artist, String album, String genre, String path)
	{
		this.artist = artist;
		this.albumName = album;
		this.songTitle = title;
		this.genre = genre;
		this.songPath = path;
	}

	public int compareTo(Song song2) {
		int comparable=0;
		
		if (this.songTitle.compareTo(song2.songTitle)<0)
		{
			comparable = -1;
		}
		
		if (this.songTitle.compareTo(song2.songTitle)>0)
		{
			comparable = 1;
		}
		
		if (this.songTitle.compareTo(song2.songTitle)==0)
		{
			comparable = 0;
		}
		
		return comparable;
	}

}
