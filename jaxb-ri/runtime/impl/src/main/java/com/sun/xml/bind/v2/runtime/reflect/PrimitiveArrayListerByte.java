/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package com.sun.xml.bind.v2.runtime.reflect;

import com.sun.xml.bind.api.AccessorException;
import com.sun.xml.bind.v2.runtime.XMLSerializer;

/**
 * {@link Lister} for primitive type arrays.
 *
 * <p>
 * B y t e ArrayLister is used as the master to generate the rest of the
 * lister classes. Do not modify the generated copies.
 */
final class PrimitiveArrayListerByte<BeanT> extends Lister<BeanT,byte[],Byte,PrimitiveArrayListerByte.ByteArrayPack> {
    
    private PrimitiveArrayListerByte() {
    }

    /*package*/ static void register() {
        Lister.primitiveArrayListers.put(Byte.TYPE,new PrimitiveArrayListerByte());
    }

    public ListIterator<Byte> iterator(final byte[] objects, XMLSerializer context) {
        return new ListIterator<Byte>() {
            int idx=0;
            public boolean hasNext() {
                return idx<objects.length;
            }

            public Byte next() {
                return objects[idx++];
            }
        };
    }

    public ByteArrayPack startPacking(BeanT current, Accessor<BeanT, byte[]> acc) {
        return new ByteArrayPack();
    }

    public void addToPack(ByteArrayPack objects, Byte o) {
        objects.add(o);
    }

    public void endPacking( ByteArrayPack pack, BeanT bean, Accessor<BeanT,byte[]> acc ) throws AccessorException {
        acc.set(bean,pack.build());
    }

    public void reset(BeanT o,Accessor<BeanT,byte[]> acc) throws AccessorException {
        acc.set(o,new byte[0]);
    }

    static final class ByteArrayPack {
        byte[] buf = new byte[16];
        int size;

        void add(Byte b) {
            if(buf.length==size) {
                // realloc
                byte[] nb = new byte[buf.length*2];
                System.arraycopy(buf,0,nb,0,buf.length);
                buf = nb;
            }
            if(b!=null)
                buf[size++] = b;
        }

        byte[] build() {
            if(buf.length==size)
                // if we are lucky enough
                return buf;

            byte[] r = new byte[size];
            System.arraycopy(buf,0,r,0,size);
            return r;
        }
    }
}
