/**
 * Autogenerated by Thrift Compiler (0.9.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package cn.edu.pku.ss.matchmaker.thrift;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestInfo implements org.apache.thrift.TBase<RequestInfo, RequestInfo._Fields>, java.io.Serializable, Cloneable {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("RequestInfo");

  private static final org.apache.thrift.protocol.TField TASK_INFO_LIST_FIELD_DESC = new org.apache.thrift.protocol.TField("taskInfoList", org.apache.thrift.protocol.TType.LIST, (short)1);
  private static final org.apache.thrift.protocol.TField GRLINFO_FIELD_DESC = new org.apache.thrift.protocol.TField("grlinfo", org.apache.thrift.protocol.TType.STRUCT, (short)2);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new RequestInfoStandardSchemeFactory());
    schemes.put(TupleScheme.class, new RequestInfoTupleSchemeFactory());
  }

  public List<TaskInfo> taskInfoList; // optional
  public GRLInfo grlinfo; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    TASK_INFO_LIST((short)1, "taskInfoList"),
    GRLINFO((short)2, "grlinfo");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // TASK_INFO_LIST
          return TASK_INFO_LIST;
        case 2: // GRLINFO
          return GRLINFO;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private _Fields optionals[] = {_Fields.TASK_INFO_LIST,_Fields.GRLINFO};
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.TASK_INFO_LIST, new org.apache.thrift.meta_data.FieldMetaData("taskInfoList", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, TaskInfo.class))));
    tmpMap.put(_Fields.GRLINFO, new org.apache.thrift.meta_data.FieldMetaData("grlinfo", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, GRLInfo.class)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(RequestInfo.class, metaDataMap);
  }

  public RequestInfo() {
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public RequestInfo(RequestInfo other) {
    if (other.isSetTaskInfoList()) {
      List<TaskInfo> __this__taskInfoList = new ArrayList<TaskInfo>();
      for (TaskInfo other_element : other.taskInfoList) {
        __this__taskInfoList.add(new TaskInfo(other_element));
      }
      this.taskInfoList = __this__taskInfoList;
    }
    if (other.isSetGrlinfo()) {
      this.grlinfo = new GRLInfo(other.grlinfo);
    }
  }

  public RequestInfo deepCopy() {
    return new RequestInfo(this);
  }

  @Override
  public void clear() {
    this.taskInfoList = null;
    this.grlinfo = null;
  }

  public int getTaskInfoListSize() {
    return (this.taskInfoList == null) ? 0 : this.taskInfoList.size();
  }

  public java.util.Iterator<TaskInfo> getTaskInfoListIterator() {
    return (this.taskInfoList == null) ? null : this.taskInfoList.iterator();
  }

  public void addToTaskInfoList(TaskInfo elem) {
    if (this.taskInfoList == null) {
      this.taskInfoList = new ArrayList<TaskInfo>();
    }
    this.taskInfoList.add(elem);
  }

  public List<TaskInfo> getTaskInfoList() {
    return this.taskInfoList;
  }

  public RequestInfo setTaskInfoList(List<TaskInfo> taskInfoList) {
    this.taskInfoList = taskInfoList;
    return this;
  }

  public void unsetTaskInfoList() {
    this.taskInfoList = null;
  }

  /** Returns true if field taskInfoList is set (has been assigned a value) and false otherwise */
  public boolean isSetTaskInfoList() {
    return this.taskInfoList != null;
  }

  public void setTaskInfoListIsSet(boolean value) {
    if (!value) {
      this.taskInfoList = null;
    }
  }

  public GRLInfo getGrlinfo() {
    return this.grlinfo;
  }

  public RequestInfo setGrlinfo(GRLInfo grlinfo) {
    this.grlinfo = grlinfo;
    return this;
  }

  public void unsetGrlinfo() {
    this.grlinfo = null;
  }

  /** Returns true if field grlinfo is set (has been assigned a value) and false otherwise */
  public boolean isSetGrlinfo() {
    return this.grlinfo != null;
  }

  public void setGrlinfoIsSet(boolean value) {
    if (!value) {
      this.grlinfo = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case TASK_INFO_LIST:
      if (value == null) {
        unsetTaskInfoList();
      } else {
        setTaskInfoList((List<TaskInfo>)value);
      }
      break;

    case GRLINFO:
      if (value == null) {
        unsetGrlinfo();
      } else {
        setGrlinfo((GRLInfo)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case TASK_INFO_LIST:
      return getTaskInfoList();

    case GRLINFO:
      return getGrlinfo();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case TASK_INFO_LIST:
      return isSetTaskInfoList();
    case GRLINFO:
      return isSetGrlinfo();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof RequestInfo)
      return this.equals((RequestInfo)that);
    return false;
  }

  public boolean equals(RequestInfo that) {
    if (that == null)
      return false;

    boolean this_present_taskInfoList = true && this.isSetTaskInfoList();
    boolean that_present_taskInfoList = true && that.isSetTaskInfoList();
    if (this_present_taskInfoList || that_present_taskInfoList) {
      if (!(this_present_taskInfoList && that_present_taskInfoList))
        return false;
      if (!this.taskInfoList.equals(that.taskInfoList))
        return false;
    }

    boolean this_present_grlinfo = true && this.isSetGrlinfo();
    boolean that_present_grlinfo = true && that.isSetGrlinfo();
    if (this_present_grlinfo || that_present_grlinfo) {
      if (!(this_present_grlinfo && that_present_grlinfo))
        return false;
      if (!this.grlinfo.equals(that.grlinfo))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  public int compareTo(RequestInfo other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    RequestInfo typedOther = (RequestInfo)other;

    lastComparison = Boolean.valueOf(isSetTaskInfoList()).compareTo(typedOther.isSetTaskInfoList());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetTaskInfoList()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.taskInfoList, typedOther.taskInfoList);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetGrlinfo()).compareTo(typedOther.isSetGrlinfo());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetGrlinfo()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.grlinfo, typedOther.grlinfo);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("RequestInfo(");
    boolean first = true;

    if (isSetTaskInfoList()) {
      sb.append("taskInfoList:");
      if (this.taskInfoList == null) {
        sb.append("null");
      } else {
        sb.append(this.taskInfoList);
      }
      first = false;
    }
    if (isSetGrlinfo()) {
      if (!first) sb.append(", ");
      sb.append("grlinfo:");
      if (this.grlinfo == null) {
        sb.append("null");
      } else {
        sb.append(this.grlinfo);
      }
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
    if (grlinfo != null) {
      grlinfo.validate();
    }
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class RequestInfoStandardSchemeFactory implements SchemeFactory {
    public RequestInfoStandardScheme getScheme() {
      return new RequestInfoStandardScheme();
    }
  }

  private static class RequestInfoStandardScheme extends StandardScheme<RequestInfo> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, RequestInfo struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // TASK_INFO_LIST
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list72 = iprot.readListBegin();
                struct.taskInfoList = new ArrayList<TaskInfo>(_list72.size);
                for (int _i73 = 0; _i73 < _list72.size; ++_i73)
                {
                  TaskInfo _elem74; // required
                  _elem74 = new TaskInfo();
                  _elem74.read(iprot);
                  struct.taskInfoList.add(_elem74);
                }
                iprot.readListEnd();
              }
              struct.setTaskInfoListIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // GRLINFO
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.grlinfo = new GRLInfo();
              struct.grlinfo.read(iprot);
              struct.setGrlinfoIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, RequestInfo struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.taskInfoList != null) {
        if (struct.isSetTaskInfoList()) {
          oprot.writeFieldBegin(TASK_INFO_LIST_FIELD_DESC);
          {
            oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.taskInfoList.size()));
            for (TaskInfo _iter75 : struct.taskInfoList)
            {
              _iter75.write(oprot);
            }
            oprot.writeListEnd();
          }
          oprot.writeFieldEnd();
        }
      }
      if (struct.grlinfo != null) {
        if (struct.isSetGrlinfo()) {
          oprot.writeFieldBegin(GRLINFO_FIELD_DESC);
          struct.grlinfo.write(oprot);
          oprot.writeFieldEnd();
        }
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class RequestInfoTupleSchemeFactory implements SchemeFactory {
    public RequestInfoTupleScheme getScheme() {
      return new RequestInfoTupleScheme();
    }
  }

  private static class RequestInfoTupleScheme extends TupleScheme<RequestInfo> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, RequestInfo struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetTaskInfoList()) {
        optionals.set(0);
      }
      if (struct.isSetGrlinfo()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.isSetTaskInfoList()) {
        {
          oprot.writeI32(struct.taskInfoList.size());
          for (TaskInfo _iter76 : struct.taskInfoList)
          {
            _iter76.write(oprot);
          }
        }
      }
      if (struct.isSetGrlinfo()) {
        struct.grlinfo.write(oprot);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, RequestInfo struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        {
          org.apache.thrift.protocol.TList _list77 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.taskInfoList = new ArrayList<TaskInfo>(_list77.size);
          for (int _i78 = 0; _i78 < _list77.size; ++_i78)
          {
            TaskInfo _elem79; // required
            _elem79 = new TaskInfo();
            _elem79.read(iprot);
            struct.taskInfoList.add(_elem79);
          }
        }
        struct.setTaskInfoListIsSet(true);
      }
      if (incoming.get(1)) {
        struct.grlinfo = new GRLInfo();
        struct.grlinfo.read(iprot);
        struct.setGrlinfoIsSet(true);
      }
    }
  }

}

