package zxjt.inte.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public enum FolderTypes {
	CONFIG {
		@Override
		public String getDefault() {
			return "config";
		}
	},
	DATA {
		@Override
		public String getDefault() {
			return "data";
		}
	},
	REPORT {
		@Override
		public String getDefault() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS");
			String time = sdf.format(new Date());
			return "report/" + time;
		}
	},
	REPOSITORY {
		@Override
		public String getDefault() {
			return "repo";
		}
	},
	RESOURCE {
		@Override
		public String getDefault() {
//			return "src/main/resources";
//			return "classes";
			return "target/classes";
		}
	},
	LOG {
		@Override
		public String getDefault() {
			return "log";
		}
	};

	public String getFolderType() {
		return this.name();
	}

	public String getVarName() {
		return "${" + this.name() + "}";
	}

	public abstract String getDefault();
}
