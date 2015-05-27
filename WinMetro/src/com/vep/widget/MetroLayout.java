package com.vep.widget;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public class MetroLayout extends RelativeLayout {

	public MetroLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MetroLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MetroLayout(Context context) {
		super(context);
	}

	private boolean synPress = true;

	public synchronized boolean getSynPress() {
		return synPress;
	}

	public synchronized void setSynPress(boolean b) {
		synPress = b;
	}

	public void showGraph() {
		if (graph != null) {
			for (int x = 0; x < row; x++) {
				System.out.println("");
				for (int y = 0; y < column; y++) {
					System.out.print(graph[x][y] + ",");
				}
			}
		}
	}

	public int[][] graph = null;
	private int row;
	private int column;
	private Context context;
	private int margin;
	private int id = 100000;// 不是很安全，可能发生id冲突
	public static SparseArray<Metro> metros;
	private static ArrayList<Integer> list = new ArrayList<Integer>();
	private int pieceH;
	private int pieceW;
	private int showMaxLine;
	private boolean isPortrait;

	/**
	 * @param context
	 * @param row
	 *            行数
	 * @param column
	 *            列数
	 * @param cellMargin
	 *            间隙
	 * @param showMaxLine
	 *            适配尺寸，在winMetro中时不再是尺寸了
	 */
	public MetroLayout(Context context, int row, int column, int cellMargin,
			int showMaxLine) {
		this(context);
		this.setTag(0);
		id = 100000;
		if (metros != null)
			metros.clear();
		list.clear();
		this.row = row;
		this.column = column;
		this.context = context;
		this.margin = cellMargin;
		this.showMaxLine = showMaxLine;
		init();
	}

	/**
	 * 初始化坐标图
	 */
	public void cleanGraph() {
		for (int x = 0; x < row; x++) {
			for (int y = 0; y < column; y++) {
				graph[x][y] = -1;
			}
		}
	}

	public void useGraph() {
		SharedPreferences sp = getContext().getSharedPreferences("CacheLayout"+getTag(),
				Context.MODE_PRIVATE);
		if (sp.getBoolean("save", false)) {
			for (int x = 0; x < row; x++) {
				for (int y = 0; y < column; y++) {
					graph[x][y] = sp.getInt(x + ":" + y, -1);
				}
			}

			if (list != null) {
				int size = list.size();
				list.clear();
				for (int i = 0; i < size; i++) {
					list.add(i, sp.getInt(i + "", -1));
				}
			}

			removeAllViews();
			cleanGraph();
			for (Integer key : list) {
				addMetroView(metros.get(key));
			}
		}
	}

	/**
	 * 保存坐标图
	 */
	public void saveGraph() {
		SharedPreferences sp = getContext().getSharedPreferences("CacheLayout"+getTag(),
				Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		for (int x = 0; x < row; x++) {
			for (int y = 0; y < column; y++) {
				edit.putInt(x + ":" + y, graph[x][y]);
			}
		}

		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				edit.putInt(i + "", list.get(i));
			}
		}

		edit.putBoolean("save", true);
		edit.commit();
	}

	private void init() {

		isPortrait = true;
		metros = new SparseArray<Metro>();
		if (row <= 0 || column <= 0) {
			throw new RuntimeException("Error for row or column");
		}

		graph = new int[row][column];// 构建数组
		cleanGraph();

		// 适配测量
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Point point = new Point();
		manager.getDefaultDisplay().getSize(point);
		int realRaw = row;
		if (row < showMaxLine) {
			realRaw = showMaxLine;
		}

		if (point.y / realRaw < point.x / column) {
			pieceH = (point.y / realRaw) * 1 - dip2px(margin);
			pieceW = (point.y / realRaw) * 1 - dip2px(margin);
		} else {
			pieceH = (point.x / column) * 1 - dip2px(margin);
			pieceW = (point.x / column) * 1 - dip2px(margin);
		}

		isPortrait = isScreenOriatationPortrait();
	}

	private boolean isScreenOriatationPortrait() {
		return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
	}

	/** dip转换px */
	public int dip2px(int dip) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f);
	}

	private int transX = -1;
	private int transY = -1;
	public static final int TORIGHT = 1;
	public static final int TOLEFT = 2;
	public static final int TOTOP = 3;
	public static final int TOBOTTOM = 4;

	public void addMetroView(Metro m) {

		if (!(m.getTag() instanceof Integer)) {
			m.setTag(id++);
			m.setId((Integer) m.getTag());
			list.add((Integer) m.getTag());
			metros.put((Integer) m.getTag(), m);
		}

		int endX = -1;
		int endY = -1;
		int w = pieceW;
		int h = pieceH;
		END: for (int x = 0; x < row; x++) {
			for (int y = 0; y < column; y++) {
				switch (m.getType()) {
				case BIG:
					m.visibleTitle(true);
					if (graph[x][y] == -1 && y + 1 < column && x + 1 < row) {
						if (graph[x][y + 1] == -1 && graph[x + 1][y] == -1
								&& graph[x + 1][y + 1] == -1) {
							graph[x][y] = (Integer) m.getTag();
							graph[x][y + 1] = (Integer) m.getTag();
							graph[x + 1][y] = (Integer) m.getTag();
							graph[x + 1][y + 1] = (Integer) m.getTag();
							endX = x;
							endY = y;
							w = w * 2 + margin * 2;
							h = h * 2 + margin * 2;

							break END;
						}
					}
				case MIDDLE:
					m.visibleTitle(true);
					if (graph[x][y] == -1 && y + 1 < column) {
						if (graph[x][y + 1] == -1) {
							graph[x][y] = (Integer) m.getTag();
							graph[x][y + 1] = (Integer) m.getTag();
							endX = x;
							endY = y;
							w = w * 2 + margin * 2;

							break END;
						}
					}
				case SMALL:
				default:
					m.visibleTitle(isPortrait);
					if (graph[x][y] == -1) {
						graph[x][y] = (Integer) m.getTag();
						endX = x;
						endY = y;
						break END;
					}

				}
			}
		}

		if (endY != -1 && endX != -1) {
			LayoutParams params = new LayoutParams(w, h);
			if (endY > 0) {
				if (graph[endX][endY - 1] != -1) {
					params.addRule(RelativeLayout.RIGHT_OF,
							graph[endX][endY - 1]);
				} else {
					if (endX + 1 < row)
						params.addRule(RelativeLayout.RIGHT_OF,
								graph[endX + 1][endY - 1]);
				}
				transX = TORIGHT;
			} else {
				params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				transX = TOLEFT;
			}

			if (endX > 0) {
				if (graph[endX - 1][endY] != -1) {
					params.addRule(RelativeLayout.BELOW, graph[endX - 1][endY]);
				} else {
					if (endY + 1 < column)
						params.addRule(RelativeLayout.BELOW,
								graph[endX - 1][endY + 1]);
				}
				transY = TOBOTTOM;
			} else {
				params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
				transY = TOTOP;
			}

			m.setLayoutParams(params);
			params.setMargins(margin, margin, margin, margin);
			addView(m.getView());
			if (flag)
				AnimationsUtils.startMove(m.getView(), transX, transY);

		}

	}

	boolean flag = false;

	public void change(int viewID, int w, int h) {

		int x = w / pieceW;
		int y = h / pieceH;

		x = x >= column ? column - 1 : x;
		y = y >= row ? row - 1 : y;

		int oldID = graph[y][x];

		int oldIndex = -1;
		int nowIndex = -1;

		if (oldID == -1) {
			if (y - 1 > 0 && graph[y - 1][x] != -1) {
				oldID = graph[y - 1][x];
			} else if (y + 1 < row && graph[y + 1][x] != -1) {
				oldID = graph[y + 1][x];
			} else if (x + 1 < column && graph[y][x + 1] != -1) {
				oldID = graph[y][x + 1];
			} else if (x - 1 > 0 && graph[y][x - 1] != -1) {
				oldID = graph[y][x - 1];
			} else {
				oldID = list.get(list.size() - 1);
			}

		}

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).equals(oldID)) {
				oldIndex = i;
			}

			if (list.get(i).equals(viewID)) {
				nowIndex = i;
			}
		}

		if (nowIndex > oldIndex)
			for (int i = nowIndex; i > oldIndex; i--) {
				list.set(i, list.get(i - 1));
			}
		else {
			for (int i = nowIndex; i < oldIndex; i++) {
				list.set(i, list.get(i + 1));
			}
		}
		list.set(oldIndex, viewID);

		removeAllViews();
		flag = true;
		cleanGraph();

		for (Integer key : list) {
			addMetroView(metros.get(key));
		}
		saveGraph();
		flag = false;
	}
}
